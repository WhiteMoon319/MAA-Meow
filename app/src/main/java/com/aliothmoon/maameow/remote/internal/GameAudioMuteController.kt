package com.aliothmoon.maameow.remote.internal

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.AudioPlaybackConfiguration
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import com.aliothmoon.maameow.constant.AndroidVersions
import com.aliothmoon.maameow.third.FakeContext
import com.aliothmoon.maameow.third.Ln
import java.util.concurrent.ConcurrentHashMap


@SuppressLint("SoonBlockedPrivateApi", "DiscouragedPrivateApi", "PrivateApi")
object GameAudioMuteController {
    private const val TAG = "GameAudioMute"

    private val mutedPackages = ConcurrentHashMap<String, Int>()

    private val audioManager: AudioManager? by lazy {
        runCatching {
            FakeContext.get().getSystemService(Context.AUDIO_SERVICE) as? AudioManager
        }.onFailure { Ln.w("$TAG: obtain AudioManager failed: ${it.message}") }.getOrNull()
    }


    private val usePlayerVolume: Boolean by lazy {
        Build.VERSION.SDK_INT >= AndroidVersions.API_33_ANDROID_13 &&
                audioManager != null &&
                getIPlayerMethod != null &&
                (getClientUidMethod != null || clientUidField != null)
    }

    private val callbackHandler by lazy {
        Handler(HandlerThread("game-audio-mute").apply { start() }.looper)
    }
    private var playbackCallback: AudioManager.AudioPlaybackCallback? = null

    private val getIPlayerMethod by lazy {
        runCatching {
            AudioPlaybackConfiguration::class.java.getDeclaredMethod("getIPlayer")
                .apply { isAccessible = true }
        }.getOrNull()
    }
    private val getClientUidMethod by lazy {
        runCatching {
            AudioPlaybackConfiguration::class.java.getDeclaredMethod("getClientUid")
                .apply { isAccessible = true }
        }.getOrNull()
    }
    private val clientUidField by lazy {
        runCatching {
            AudioPlaybackConfiguration::class.java.getDeclaredField("mClientUid")
                .apply { isAccessible = true }
        }.getOrNull()
    }


    @Synchronized
    fun setMuted(packageName: String, muted: Boolean): Boolean {
        return if (muted) mute(packageName) else unmute(packageName)
    }

    @Synchronized
    fun restoreAll() {
        mutedPackages.keys.toList().forEach { unmute(it) }
    }

    @Synchronized
    private fun mute(pkg: String): Boolean {
        if (usePlayerVolume) {
            val uid = RemoteUtils.getAppUid(pkg)
            if (uid < 0) {
                Ln.w("$TAG: mute $pkg failed - cannot resolve uid")
                return false
            }
            mutedPackages[pkg] = uid
            applyVolumeToUid(uid, 0f)
            ensureCallback()
            Ln.i("$TAG: muted $pkg via IPlayer volume (uid=$uid)")
            return true
        }

        val ok = AppOpsHelper.setPlayAudioOpAllowed(pkg, false)
        if (ok) {
            mutedPackages[pkg] = -1
            Ln.i("$TAG: muted $pkg via appops")
        } else {
            Ln.w("$TAG: appops mute failed for $pkg")
        }
        return ok
    }

    @Synchronized
    private fun unmute(pkg: String): Boolean {
        // 无条件尝试恢复：appops 状态写进系统、跨远端进程存活，即使本进程没跟踪该包
        mutedPackages.remove(pkg)
        if (usePlayerVolume) {
            RemoteUtils.getAppUid(pkg).takeIf { it >= 0 }?.let { applyVolumeToUid(it, 1f) }
            maybeStopCallback()
        } else {
            AppOpsHelper.setPlayAudioOpAllowed(pkg, true)
        }
        return true
    }

    private fun applyVolumeToUid(uid: Int, volume: Float) {
        val am = audioManager ?: return
        val configs = runCatching { am.activePlaybackConfigurations }.getOrElse {
            Ln.w("$TAG: getActivePlaybackConfigurations failed: ${it.message}")
            return
        }
        var applied = 0
        for (cfg in configs) {
            if (clientUidOf(cfg) != uid) continue
            iPlayerOf(cfg)?.let { if (setPlayerVolume(it, volume)) applied++ }
        }
        if (volume == 0f && applied == 0) {
            // 多为：本进程拿不到真实 IPlayer（无权限）或目标此刻未播放；前者会导致静音不生效
            Ln.w("$TAG: applyVolume(0) matched no player for uid=$uid (no privilege or not playing yet)")
        }
    }

    private fun ensureCallback() {
        if (playbackCallback != null) return
        val am = audioManager ?: return
        val cb = object : AudioManager.AudioPlaybackCallback() {
            override fun onPlaybackConfigChanged(configs: MutableList<AudioPlaybackConfiguration>) {
                // 游戏新建播放器时把目标 uid 的音量重新压到 0
                val mutedUids = mutedPackages.values
                if (mutedUids.isEmpty()) return
                for (cfg in configs) {
                    if (clientUidOf(cfg) in mutedUids) {
                        iPlayerOf(cfg)?.let { setPlayerVolume(it, 0f) }
                    }
                }
            }
        }
        runCatching { am.registerAudioPlaybackCallback(cb, callbackHandler) }
            .onSuccess { playbackCallback = cb }
            .onFailure { Ln.w("$TAG: registerAudioPlaybackCallback failed: ${it.message}") }
    }

    private fun maybeStopCallback() {
        if (mutedPackages.isNotEmpty()) return
        val cb = playbackCallback ?: return
        runCatching { audioManager?.unregisterAudioPlaybackCallback(cb) }
        playbackCallback = null
    }

    private fun iPlayerOf(cfg: AudioPlaybackConfiguration): Any? =
        runCatching { getIPlayerMethod?.invoke(cfg) }.getOrNull()

    private fun clientUidOf(cfg: AudioPlaybackConfiguration): Int = runCatching {
        (getClientUidMethod?.invoke(cfg) as? Int) ?: (clientUidField?.get(cfg) as? Int) ?: -1
    }.getOrDefault(-1)

    private fun setPlayerVolume(player: Any, volume: Float): Boolean = runCatching {
        player.javaClass
            .getMethod("setVolume", Float::class.javaPrimitiveType)
            .invoke(player, volume)
        true
    }.getOrElse { false }
}
