package com.aliothmoon.maameow.presentation.view.settings

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.aliothmoon.maameow.R
import com.aliothmoon.maameow.presentation.components.AdaptiveScaffold
import com.aliothmoon.maameow.presentation.components.TopAppBar
import com.aliothmoon.maameow.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel
import top.yukonga.miuix.kmp.basic.Card as MiuixCard
import top.yukonga.miuix.kmp.basic.ColorPicker as MiuixColorPicker
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme

private val MiuixPresetKeyColors = listOf(
    Color(0xFFFF8A80),
    Color(0xFFE91E63),
    Color(0xFF9C27B0),
    Color(0xFF673AB7),
    Color(0xFF3F51B5),
    Color(0xFF2196F3),
    Color(0xFF00BCD4),
    Color(0xFF009688),
    Color(0xFF4FAF50),
    Color(0xFFFFEB3B),
    Color(0xFFFFC107),
    Color(0xFFFF9800),
    Color(0xFF795548),
    Color(0xFF607D8F),
    Color(0xFFFF9CA8)
)

private fun parseArgbColor(color: String): Color? {
    val value = color.removePrefix("#")
    val argb = when (value.length) {
        6 -> "FF$value"
        8 -> value
        else -> return null
    }
    return argb.toULongOrNull(16)?.let { Color(it.toInt()) }
}

@Composable
fun PersonalizationView(
    navController: NavController,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val useMiuixDynamicColor by viewModel.useMiuixDynamicColor.collectAsStateWithLifecycle()
    val miuixKeyColor by viewModel.miuixKeyColor.collectAsStateWithLifecycle()
    val enableFloatingBottomBar by viewModel.enableMiuixFloatingBottomBar.collectAsStateWithLifecycle()
    val enableLiquidGlass by viewModel.enableMiuixLiquidGlass.collectAsStateWithLifecycle()
    val enablePredictiveBack by viewModel.enablePredictiveBack.collectAsStateWithLifecycle()

    AdaptiveScaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.personalization_title),
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = { navController.navigateUp() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                MiuixThemePreviewCard(
                    keyColor = miuixKeyColor,
                    dynamicColorEnabled = useMiuixDynamicColor,
                    floatingBottomBarEnabled = enableFloatingBottomBar,
                    liquidGlassEnabled = enableLiquidGlass
                )
            }
            item {
                MiuixCard(insideMargin = PaddingValues(vertical = 0.dp)) {
                    SwitchPreference(
                        title = stringResource(R.string.settings_use_miuix_dynamic_color_title),
                        summary = stringResource(R.string.settings_use_miuix_dynamic_color_desc),
                        checked = useMiuixDynamicColor,
                        onCheckedChange = { viewModel.setUseMiuixDynamicColor(it) }
                    )
                    MiuixColorSettingsItem(
                        keyColor = miuixKeyColor,
                        dynamicColorEnabled = useMiuixDynamicColor,
                        onColorChanged = { viewModel.setMiuixKeyColor(it) },
                        onClearColor = { viewModel.setMiuixKeyColor("") }
                    )
                }
            }
            item {
                MiuixCard(insideMargin = PaddingValues(vertical = 0.dp)) {
                    SwitchPreference(
                        title = stringResource(R.string.settings_miuix_floating_bottom_bar_title),
                        summary = stringResource(R.string.settings_miuix_floating_bottom_bar_desc),
                        checked = enableFloatingBottomBar,
                        onCheckedChange = { viewModel.setEnableMiuixFloatingBottomBar(it) }
                    )
                    SwitchPreference(
                        title = stringResource(R.string.settings_miuix_liquid_glass_title),
                        summary = stringResource(R.string.settings_miuix_liquid_glass_desc),
                        checked = enableLiquidGlass,
                        enabled = enableFloatingBottomBar,
                        onCheckedChange = { viewModel.setEnableMiuixLiquidGlass(it) }
                    )
                    SwitchPreference(
                        title = stringResource(R.string.settings_predictive_back_title),
                        summary = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            stringResource(R.string.settings_predictive_back_desc)
                        } else {
                            stringResource(R.string.settings_predictive_back_unsupported_desc)
                        },
                        checked = enablePredictiveBack,
                        enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU,
                        onCheckedChange = { viewModel.setEnablePredictiveBack(it) }
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun MiuixThemePreviewCard(
    keyColor: String,
    dynamicColorEnabled: Boolean,
    floatingBottomBarEnabled: Boolean,
    liquidGlassEnabled: Boolean
) {
    val seedColor = parseArgbColor(keyColor) ?: MiuixTheme.colorScheme.primary
    val navColor = MiuixTheme.colorScheme.surface.copy(alpha = if (liquidGlassEnabled) 0.34f else 1f)
    MiuixCard(insideMargin = PaddingValues(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 124.dp, height = 184.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(MiuixTheme.colorScheme.background)
                    .border(1.dp, MiuixTheme.colorScheme.dividerLine, RoundedCornerShape(28.dp))
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.64f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(seedColor)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(seedColor.copy(alpha = 0.24f))
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MiuixTheme.colorScheme.surfaceContainer)
                        )
                    }
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(18.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MiuixTheme.colorScheme.surfaceContainer)
                        )
                    }
                }
                if (floatingBottomBarEnabled) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(navColor)
                            .border(0.5.dp, MiuixTheme.colorScheme.dividerLine, RoundedCornerShape(18.dp))
                            .padding(horizontal = 14.dp, vertical = 9.dp),
                        horizontalArrangement = Arrangement.spacedBy(9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(4) { index ->
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (index == 0) seedColor else MiuixTheme.colorScheme.onSurfaceVariantSummary)
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                top.yukonga.miuix.kmp.basic.Text(
                    text = stringResource(R.string.settings_miuix_key_color_preview),
                    style = MiuixTheme.textStyles.headline1,
                    color = MiuixTheme.colorScheme.onSurface
                )
                top.yukonga.miuix.kmp.basic.Text(
                    text = if (dynamicColorEnabled && keyColor.isBlank()) {
                        stringResource(R.string.settings_miuix_key_color_dynamic_notice)
                    } else {
                        "#${seedColor.toArgb().toUInt().toString(16).uppercase()}"
                    },
                    style = MiuixTheme.textStyles.footnote1,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                )
            }
        }
    }
}

@Composable
private fun MiuixColorSettingsItem(
    keyColor: String,
    dynamicColorEnabled: Boolean,
    onColorChanged: (String) -> Unit,
    onClearColor: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val defaultColor = MiuixTheme.colorScheme.primary
    val selectedColor = remember(keyColor, defaultColor) {
        parseArgbColor(keyColor) ?: defaultColor
    }

    MiuixSettingGroup(
        title = stringResource(R.string.settings_miuix_key_color_title),
        summary = stringResource(R.string.settings_miuix_key_color_desc)
    ) {
        ArrowPreference(
            title = stringResource(R.string.settings_miuix_key_color_preview),
            summary = if (dynamicColorEnabled && keyColor.isBlank()) {
                stringResource(R.string.settings_miuix_key_color_dynamic_notice)
            } else {
                "#${selectedColor.toArgb().toUInt().toString(16).uppercase()}"
            },
            onClick = { expanded = !expanded }
        )
        if (expanded) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MiuixPresetKeyColors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(color)
                                .border(
                                    width = if (color.toArgb() == selectedColor.toArgb()) 2.dp else 0.5.dp,
                                    color = if (color.toArgb() == selectedColor.toArgb()) {
                                        MiuixTheme.colorScheme.onSurface
                                    } else {
                                        MiuixTheme.colorScheme.dividerLine
                                    },
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .clickable { onColorChanged(color.toArgb().toUInt().toString(16).uppercase()) }
                        )
                    }
                }
                ArrowPreference(
                    title = stringResource(R.string.settings_miuix_key_color_clear),
                    summary = stringResource(R.string.settings_miuix_key_color_clear_desc),
                    onClick = onClearColor
                )
                MiuixColorPicker(
                    color = selectedColor,
                    onColorChanged = { color ->
                        onColorChanged(color.toArgb().toUInt().toString(16).uppercase())
                    },
                    modifier = Modifier.fillMaxWidth(),
                    showPreview = true
                )
                top.yukonga.miuix.kmp.basic.Text(
                    text = stringResource(R.string.settings_miuix_key_color_preview_hint),
                    style = MiuixTheme.textStyles.footnote1,
                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                )
            }
        }
    }
}

@Composable
private fun MiuixSettingGroup(
    title: String,
    summary: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        top.yukonga.miuix.kmp.basic.Text(
            text = title,
            style = MiuixTheme.textStyles.footnote1,
            color = MiuixTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )
        if (summary != null) {
            top.yukonga.miuix.kmp.basic.Text(
                text = summary,
                style = MiuixTheme.textStyles.footnote1,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 4.dp)
            )
        }
        content()
    }
}
