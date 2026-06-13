package com.aliothmoon.maameow.manager

import android.content.Context
import android.os.Build
import com.aliothmoon.maameow.adb.AdbClient
import com.aliothmoon.maameow.adb.AdbKey
import com.aliothmoon.maameow.adb.AdbMdns
import com.aliothmoon.maameow.adb.AdbPairingService
import com.aliothmoon.maameow.adb.PreferenceAdbKeyStore
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.net.ssl.SSLProtocolException

object ShizukuSetupManager {

    private const val TAG = "ShizukuSetupManager"
    private const val SHIZUKU_SERVER_CLASS = "rikka.shizuku.server.ShizukuService"
    private const val POLL_INTERVAL = 1500L
    private const val MAX_ATTEMPTS = 40
    private const val ADB_TIMEOUT = 15_000L

    enum class SetupState {
        IDLE,
        STARTING,
        WAITING_FOR_WADB,
        SEARCHING_ADB,
        WAITING_FOR_PAIR,
        RUNNING,
        ERROR
    }

    data class SetupStatus(
        val state: SetupState = SetupState.IDLE,
        val message: String = "",
        val error: String? = null
    )

    private val _setupState = MutableStateFlow(SetupStatus())
    val setupState: StateFlow<SetupStatus> = _setupState.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    @Volatile
    private var isSetupRunning = false

    fun startSetup(context: Context) {
        if (isSetupRunning) return
        isSetupRunning = true

        scope.launch {
            try {
                performSetup(context)
            } catch (e: Exception) {
                Timber.e(e, "Setup failed")
                _setupState.value = SetupStatus(state = SetupState.ERROR, error = e.message)
            } finally {
                isSetupRunning = false
            }
        }
    }

    private suspend fun performSetup(context: Context) {
        val apkPath = context.applicationInfo.sourceDir

        // Step 1: 尝试 Root 启动
        _setupState.value = SetupStatus(state = SetupState.STARTING, message = "正在尝试 Root 启动…")
        if (Shell.getShell().isRoot) {
            if (startServerWithRoot(apkPath) && waitForShizukuReady()) {
                _setupState.value = SetupStatus(state = SetupState.RUNNING)
                return
            }
        }

        // Step 2: 无 Root，检查无线调试
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            throw Exception("Android 11 以下需要 Root 权限")
        }

        _setupState.value = SetupStatus(state = SetupState.WAITING_FOR_WADB, message = "正在检查无线调试…")
        if (!isWirelessDebugEnabled()) {
            waitForWirelessDebug()
        }

        // Step 3: mDNS 发现 ADB 端口
        _setupState.value = SetupStatus(state = SetupState.SEARCHING_ADB, message = "正在搜索 ADB 端口…")
        val port = discoverAdbPort(context)
        if (port <= 0) {
            throw Exception("无法发现 ADB 端口，请确认无线调试已启用")
        }

        // Step 4: 通过 ADB 连接启动 Shizuku
        _setupState.value = SetupStatus(state = SetupState.STARTING, message = "正在连接 ADB (端口 $port)…")
        try {
            startServerViaAdb(context, "127.0.0.1", port, apkPath)
        } catch (e: SSLProtocolException) {
            // TLS 握手失败，需要配对
            Timber.w("ADB TLS failed, need pairing")
            _setupState.value = SetupStatus(state = SetupState.WAITING_FOR_PAIR, message = "需要 ADB 配对")

            // 启动配对服务
            context.startForegroundService(AdbPairingService.startIntent(context))

            // 等待配对完成后再重试
            waitForPairingAndRetry(context, apkPath)
            return
        }

        if (!waitForShizukuReady()) {
            throw Exception("Shizuku 启动超时")
        }

        _setupState.value = SetupStatus(state = SetupState.RUNNING)
    }

    private suspend fun waitForPairingAndRetry(context: Context, apkPath: String) {
        // 等待配对完成（用户在通知中输入配对码后，配对服务会自动完成）
        var retries = 0
        while (retries < 60) { // 最多等 90 秒
            delay(POLL_INTERVAL)

            // 检查 Shizuku 是否已启动
            if (ShizukuManager.isShizukuAvailable()) {
                _setupState.value = SetupStatus(state = SetupState.RUNNING)
                return
            }

            // 尝试重新连接
            try {
                val port = discoverAdbPort(context)
                if (port > 0) {
                    _setupState.value = SetupStatus(state = SetupState.STARTING, message = "正在重试连接…")
                    startServerViaAdb(context, "127.0.0.1", port, apkPath)
                    if (waitForShizukuReady()) {
                        _setupState.value = SetupStatus(state = SetupState.RUNNING)
                        return
                    }
                }
            } catch (_: Exception) {
                // 配对可能还没完成，继续等待
            }

            retries++
        }

        throw Exception("配对超时，请重试")
    }

    private fun startServerWithRoot(apkPath: String): Boolean {
        return try {
            val cmd = "CLASSPATH=\"$apkPath\" app_process /system/bin $SHIZUKU_SERVER_CLASS"
            Shell.cmd(cmd).exec()
            true
        } catch (e: Exception) {
            Timber.e(e, "Root start failed")
            false
        }
    }

    private fun startServerViaAdb(context: Context, host: String, port: Int, apkPath: String) {
        val keyStore = PreferenceAdbKeyStore(context.getSharedPreferences("adb_key", Context.MODE_PRIVATE))
        val key = AdbKey(keyStore, "maameow")

        AdbClient(host, port, key).use { client ->
            client.connect()
            client.shellCommand("CLASSPATH=\"$apkPath\" app_process /system/bin $SHIZUKU_SERVER_CLASS") { output ->
                Timber.d("ADB shell output: ${String(output)}")
            }
        }
    }

    private suspend fun discoverAdbPort(context: Context): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return -1

        return kotlinx.coroutines.suspendCancellableCoroutine { cont ->
            val mdns = AdbMdns(context, AdbMdns.TLS_CONNECT) { port ->
                if (port > 0 && cont.isActive) {
                    cont.resume(port) {}
                }
            }
            mdns.start()

            scope.launch {
                delay(ADB_TIMEOUT)
                if (cont.isActive) {
                    mdns.stop()
                    cont.resume(-1) {}
                }
            }

            cont.invokeOnCancellation { mdns.stop() }
        }
    }

    private fun isWirelessDebugEnabled(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) return false
        return try {
            val result = Shell.cmd("settings get global adb_wifi_enabled").exec()
            result.out.joinToString("").trim() == "1"
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun waitForWirelessDebug() {
        var attempts = 0
        while (!isWirelessDebugEnabled() && attempts < MAX_ATTEMPTS) {
            delay(POLL_INTERVAL)
            attempts++
        }
        if (!isWirelessDebugEnabled()) {
            throw Exception("等待启用无线调试超时")
        }
    }

    private suspend fun waitForShizukuReady(): Boolean {
        repeat(MAX_ATTEMPTS) {
            if (ShizukuManager.isShizukuAvailable()) return true
            delay(POLL_INTERVAL)
        }
        return false
    }

    fun reset() {
        _setupState.value = SetupStatus()
        isSetupRunning = false
    }
}
