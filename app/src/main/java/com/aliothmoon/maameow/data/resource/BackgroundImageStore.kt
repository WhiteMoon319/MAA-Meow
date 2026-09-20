package com.aliothmoon.maameow.data.resource

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.exifinterface.media.ExifInterface
import com.aliothmoon.maameow.data.preferences.AppSettingsManager
import com.aliothmoon.maameow.utils.Misc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.time.LocalDate

/**
 * 自定义主界面背景图的单一数据源（支持多图与轮播）。
 *
 * 职责：
 * - [prepareSource] / [decodeSource]：把用户选中的图片复制到缓存并按 EXIF 方向解码，供裁剪页使用；
 * - [addCropped]：把裁剪结果追加为一张新背景（filesDir/backgrounds/bg_<id>.jpg）并选中；
 * - [removeImage] / [clear]：删除单张或全部背景；
 * - [rotateIfNeeded]：按偏好（每次启动 / 每天，可随机）切换当前图；
 * - [imageBitmap]：监听「启用状态 + 令牌」，解码当前图供主界面绘制。
 *
 * 只负责数据与解码，不含任何 UI；玻璃主题与遮罩绘制在 presentation/theme 层完成。
 */
class BackgroundImageStore(
    private val context: Context,
    private val appSettingsManager: AppSettingsManager,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // 串行化背景文件的写入/删除：NonCancellable 的保存可能在转屏后仍在后台执行，
    // 重建后的页面再次保存会与之共写同一临时文件，必须互斥。
    private val writeMutex = Mutex()

    // LAUNCH 模式下，进程内只轮播一次，避免转屏重建重复切换。
    @Volatile
    private var launchRotated = false

    private val backgroundsDir: File
        get() = File(context.filesDir, DIR_NAME)

    // 旧版单图文件名，仅用于迁移。
    private val legacyFile: File
        get() = File(backgroundsDir, LEGACY_FILE_NAME)

    /** 当前生效的背景位图；未启用或无文件时为 null。scope 即 IO 调度器，解码在 IO 线程执行。 */
    @OptIn(ExperimentalCoroutinesApi::class)
    val imageBitmap: StateFlow<ImageBitmap?> =
        combine(
            appSettingsManager.customBackgroundEnabled,
            appSettingsManager.customBackgroundToken,
        ) { enabled, token -> enabled to token }
            .mapLatest { (enabled, _) -> if (enabled) loadCurrentBitmap() else null }
            .stateIn(scope, SharingStarted.Eagerly, null)

    /**
     * 把选中的图片复制到缓存目录，返回文件路径。
     * 裁剪页从该文件解码，进程被杀重建后仍可恢复（此时图片选择器的 Uri 授权通常已失效）。
     */
    suspend fun prepareSource(uri: Uri): String? = withContext(Dispatchers.IO) {
        runCatching {
            val sourceFile = File(context.cacheDir, SOURCE_TMP_NAME)
            context.contentResolver.openInputStream(uri)?.use { input ->
                sourceFile.outputStream().use { output -> input.copyTo(output) }
            } ?: return@runCatching null
            sourceFile.absolutePath
        }.onFailure { Timber.e(it, "prepareSource failed") }.getOrNull()
    }

    /**
     * 解码裁剪源图片：按 EXIF 方向旋转/镜像，并降采样限制内存占用。
     * EXIF 解析失败时按正常方向继续解码。
     */
    suspend fun decodeSource(path: String): Bitmap? = withContext(Dispatchers.IO) {
        runCatching {
            val orientation = runCatching {
                ExifInterface(path).getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL,
                )
            }.getOrDefault(ExifInterface.ORIENTATION_NORMAL)

            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(path, bounds)
            if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return@runCatching null

            var sample = 1
            while (bounds.outWidth / sample > MAX_SOURCE_SIDE || bounds.outHeight / sample > MAX_SOURCE_SIDE) {
                sample *= 2
            }
            val decoded = BitmapFactory.decodeFile(
                path,
                BitmapFactory.Options().apply { inSampleSize = sample },
            ) ?: return@runCatching null

            if (orientation == ExifInterface.ORIENTATION_NORMAL ||
                orientation == ExifInterface.ORIENTATION_UNDEFINED
            ) {
                return@runCatching decoded
            }
            val matrix = Matrix().apply {
                when (orientation) {
                    ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> postScale(-1f, 1f)
                    ExifInterface.ORIENTATION_ROTATE_180 -> postRotate(180f)
                    ExifInterface.ORIENTATION_FLIP_VERTICAL -> postScale(1f, -1f)
                    ExifInterface.ORIENTATION_TRANSPOSE -> {
                        postRotate(90f)
                        postScale(-1f, 1f)
                    }

                    ExifInterface.ORIENTATION_ROTATE_90 -> postRotate(90f)
                    ExifInterface.ORIENTATION_TRANSVERSE -> {
                        postRotate(-90f)
                        postScale(-1f, 1f)
                    }

                    ExifInterface.ORIENTATION_ROTATE_270 -> postRotate(270f)
                }
            }
            try {
                Bitmap.createBitmap(decoded, 0, 0, decoded.width, decoded.height, matrix, true)
            } finally {
                decoded.recycle()
            }
        }.onFailure { Timber.e(it, "decodeSource failed") }.getOrNull()
    }

    /** 删除裁剪源缓存文件（取消裁剪或保存完成后调用）。 */
    fun clearSourceCache() {
        scope.launch { runCatching { File(context.cacheDir, SOURCE_TMP_NAME).delete() } }
    }

    /**
     * 把裁剪结果追加为一张新背景并设为当前图；成功后同步启用态与令牌。
     */
    suspend fun addCropped(bitmap: Bitmap): Boolean =
        withContext(NonCancellable + Dispatchers.IO) {
            writeMutex.withLock {
                runCatching {
                    backgroundsDir.mkdirs()
                    val id = System.currentTimeMillis().toString()
                    val target = File(backgroundsDir, fileFor(id))
                    // 先写临时文件再同目录原子重命名：压缩失败或进程被杀不会留下半截文件。
                    val temporaryFile = File(backgroundsDir, "${target.name}.tmp")
                    try {
                        temporaryFile.outputStream().use { out ->
                            check(bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
                        }
                        check(temporaryFile.renameTo(target)) { "重命名背景文件失败" }
                    } finally {
                        temporaryFile.delete()
                    }
                    val ids = parseIds(appSettingsManager.customBackgroundImageIds.value) + id
                    appSettingsManager.setCustomBackgroundImages(ids, id)
                    true
                }.onFailure { Timber.e(it, "addCropped failed") }.getOrDefault(false)
            }
        }

    /** 删除指定背景图；若删的是当前图则切换到剩余的第一张，删空则关闭背景。 */
    suspend fun removeImage(id: String) = withContext(Dispatchers.IO) {
        writeMutex.withLock {
            val ids = parseIds(appSettingsManager.customBackgroundImageIds.value)
            if (id !in ids) return@withLock
            runCatching { File(backgroundsDir, fileFor(id)).delete() }
            val remaining = ids - id
            val current = appSettingsManager.customBackgroundCurrentId.value
            val next = if (current == id) remaining.firstOrNull().orEmpty() else current
            appSettingsManager.setCustomBackgroundImages(remaining, next)
        }
    }

    /** 关闭并清除全部自定义背景。 */
    suspend fun clear() = withContext(Dispatchers.IO) {
        writeMutex.withLock {
            backgroundsDir.listFiles()?.forEach { runCatching { it.delete() } }
            appSettingsManager.setCustomBackgroundImages(emptyList(), "")
        }
    }

    /**
     * 按偏好轮播当前图：LAUNCH 每次启动一次，DAILY 跨天一次；均需至少两张图。
     * 随机模式从其余图中挑选，顺序模式取下一张。
     */
    suspend fun rotateIfNeeded() = withContext(Dispatchers.IO) {
        val mode = appSettingsManager.customBackgroundRotateMode.value
        if (mode == MODE_OFF) return@withContext
        val ids = parseIds(appSettingsManager.customBackgroundImageIds.value)
        if (ids.size < 2) return@withContext
        val today = LocalDate.now().toString()
        when (mode) {
            MODE_LAUNCH -> {
                if (launchRotated) return@withContext
                launchRotated = true
            }

            MODE_DAILY -> if (appSettingsManager.customBackgroundLastRotateDate.value == today) {
                return@withContext
            }

            else -> return@withContext
        }
        val current = appSettingsManager.customBackgroundCurrentId.value
        val shuffle = appSettingsManager.customBackgroundShuffle.value
        val next = if (shuffle) {
            ids.filter { it != current }.random()
        } else {
            val index = ids.indexOf(current).let { if (it < 0) 0 else it }
            ids[(index + 1) % ids.size]
        }
        runCatching { appSettingsManager.setCustomBackgroundRotated(next, today) }
            .onFailure { Timber.e(it, "rotateIfNeeded failed") }
    }

    private suspend fun loadCurrentBitmap(): ImageBitmap? {
        if (appSettingsManager.customBackgroundFollowSystem.value) {
            systemWallpaperBitmap()?.let { return it }
            // 取不到系统壁纸时回退本地图片
        }
        migrateLegacyIfNeeded()
        val ids = parseIds(appSettingsManager.customBackgroundImageIds.value)
        val current = appSettingsManager.customBackgroundCurrentId.value
            .ifBlank { ids.firstOrNull().orEmpty() }
        if (current.isBlank()) return null
        val (screenWidth, screenHeight) = Misc.getScreenSize(context)
        val file = File(backgroundsDir, fileFor(current))
        return decodeScaled(file, screenWidth, screenHeight)?.asImageBitmap()
    }

    /** 读取系统壁纸；部分版本可能无权限或返回空，失败一律返回 null 由调用方回退。 */
    private fun systemWallpaperBitmap(): ImageBitmap? = runCatching {
        val drawable = WallpaperManager.getInstance(context).drawable ?: return@runCatching null
        val bitmap = if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val width = drawable.intrinsicWidth
            val height = drawable.intrinsicHeight
            if (width <= 0 || height <= 0) return@runCatching null
            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also { target ->
                drawable.setBounds(0, 0, width, height)
                drawable.draw(Canvas(target))
            }
        }
        bitmap.asImageBitmap()
    }.onFailure { Timber.e(it, "read system wallpaper failed") }.getOrNull()

    /** 旧版单图 bg.jpg 迁移为多图列表首项。 */
    private suspend fun migrateLegacyIfNeeded() = writeMutex.withLock {
        if (appSettingsManager.customBackgroundImageIds.value.isNotBlank()) return@withLock
        if (!legacyFile.exists()) return@withLock
        val id = System.currentTimeMillis().toString()
        val target = File(backgroundsDir, fileFor(id))
        if (legacyFile.renameTo(target)) {
            appSettingsManager.setCustomBackgroundImages(listOf(id), id)
        }
    }

    private fun parseIds(raw: String): List<String> =
        raw.split(',').map { it.trim() }.filter { it.isNotBlank() }

    private fun fileFor(id: String): String = "bg_$id.jpg"

    private fun decodeScaled(file: File, requestedWidth: Int, requestedHeight: Int): Bitmap? {
        if (!file.exists() || file.length() == 0L) return null
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(file.absolutePath, bounds)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        val options = BitmapFactory.Options().apply {
            inSampleSize = calculateInSampleSize(
                width = bounds.outWidth,
                height = bounds.outHeight,
                requestedWidth = requestedWidth,
                requestedHeight = requestedHeight,
            )
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        return BitmapFactory.decodeFile(file.absolutePath, options)
    }

    private fun calculateInSampleSize(
        width: Int,
        height: Int,
        requestedWidth: Int,
        requestedHeight: Int,
    ): Int {
        if (requestedWidth <= 0 || requestedHeight <= 0) return 1
        var sample = 1
        val halfWidth = width / 2
        val halfHeight = height / 2
        while (halfWidth / sample >= requestedWidth && halfHeight / sample >= requestedHeight) {
            sample *= 2
        }
        return sample
    }

    companion object {
        private const val DIR_NAME = "backgrounds"
        private const val LEGACY_FILE_NAME = "bg.jpg"
        private const val SOURCE_TMP_NAME = "bg_source_tmp"

        /** 裁剪源图片解码的最长边限制 */
        private const val MAX_SOURCE_SIDE = 2400

        /** 轮播模式 */
        const val MODE_OFF = "OFF"
        const val MODE_LAUNCH = "LAUNCH"
        const val MODE_DAILY = "DAILY"
    }
}
