package com.aliothmoon.maameow.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.google.android.material.color.utilities.DynamicScheme
import com.google.android.material.color.utilities.Hct
import com.google.android.material.color.utilities.QuantizerCelebi
import com.google.android.material.color.utilities.SchemeTonalSpot
import com.google.android.material.color.utilities.Score

/**
 * 原生莫奈取色：用 Material Color Utilities（与系统动态取色同一套算法）从背景图提取种子色，
 * 生成整套 [ColorScheme]。
 *
 * 图片过小或无法取色时返回 null，由调用方回退到原配色。
 */
fun monetColorScheme(image: ImageBitmap, dark: Boolean): ColorScheme? {
    val seed = extractSeedColor(image) ?: return null
    return SchemeTonalSpot(Hct.fromInt(seed), dark, 0.0).toComposeColorScheme()
}

/** 降采样到约 64 像素量级后量化取种子色。 */
private fun extractSeedColor(image: ImageBitmap): Int? {
    val width = image.width
    val height = image.height
    if (width <= 0 || height <= 0) return null
    return runCatching {
        val buffer = IntArray(width * height)
        image.readPixels(buffer)
        val step = (minOf(width, height) / 64).coerceAtLeast(1)
        val sampled = ArrayList<Int>((width / step + 1) * (height / step + 1))
        var y = 0
        while (y < height) {
            var x = 0
            while (x < width) {
                sampled.add(buffer[y * width + x])
                x += step
            }
            y += step
        }
        if (sampled.isEmpty()) return null
        val quantized = QuantizerCelebi.quantize(sampled.toIntArray(), 128)
        Score.score(quantized).firstOrNull()
    }.getOrNull()
}

/** 把 MCU 动态方案映射为 Compose 配色。 */
private fun DynamicScheme.toComposeColorScheme(): ColorScheme {
    fun color(value: Int) = Color(value)
    return (if (isDark) darkColorScheme() else lightColorScheme()).copy(
        primary = color(primary),
        onPrimary = color(onPrimary),
        primaryContainer = color(primaryContainer),
        onPrimaryContainer = color(onPrimaryContainer),
        secondary = color(secondary),
        onSecondary = color(onSecondary),
        secondaryContainer = color(secondaryContainer),
        onSecondaryContainer = color(onSecondaryContainer),
        tertiary = color(tertiary),
        onTertiary = color(onTertiary),
        tertiaryContainer = color(tertiaryContainer),
        onTertiaryContainer = color(onTertiaryContainer),
        error = color(error),
        onError = color(onError),
        errorContainer = color(errorContainer),
        onErrorContainer = color(onErrorContainer),
        background = color(background),
        onBackground = color(onBackground),
        surface = color(surface),
        onSurface = color(onSurface),
        surfaceVariant = color(surfaceVariant),
        onSurfaceVariant = color(onSurfaceVariant),
        surfaceTint = color(primary),
        inverseSurface = color(inverseSurface),
        inverseOnSurface = color(inverseOnSurface),
        inversePrimary = color(inversePrimary),
        outline = color(outline),
        outlineVariant = color(outlineVariant),
        scrim = color(scrim),
        surfaceBright = color(surfaceBright),
        surfaceDim = color(surfaceDim),
        surfaceContainer = color(surfaceContainer),
        surfaceContainerHigh = color(surfaceContainerHigh),
        surfaceContainerHighest = color(surfaceContainerHighest),
        surfaceContainerLow = color(surfaceContainerLow),
        surfaceContainerLowest = color(surfaceContainerLowest),
    )
}
