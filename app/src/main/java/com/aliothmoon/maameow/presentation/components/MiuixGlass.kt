package com.aliothmoon.maameow.presentation.components

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurColors
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.textureBlur
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun Modifier.miuixLiquidGlass(
    backdrop: LayerBackdrop?,
    shape: Shape,
    enabled: Boolean = true
): Modifier {
    if (!enabled || backdrop == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        return this
    }

    return textureBlur(
        backdrop = backdrop,
        shape = shape,
        blurRadius = 42f,
        colors = BlurColors(
            blendColors = listOf(
                BlendColorEntry(MiuixTheme.colorScheme.surface.copy(alpha = 0.46f)),
                BlendColorEntry(MiuixTheme.colorScheme.primary.copy(alpha = 0.10f))
            ),
            brightness = 0.04f,
            contrast = 1.08f,
            saturation = 1.45f
        )
    )
}
