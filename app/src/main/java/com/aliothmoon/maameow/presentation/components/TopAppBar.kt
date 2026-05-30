package com.aliothmoon.maameow.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon as MaterialIcon
import androidx.compose.material3.IconButton as MaterialIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar as MaterialTopAppBar
import androidx.compose.material3.TopAppBarDefaults as MaterialTopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.aliothmoon.maameow.R
import com.aliothmoon.maameow.theme.LocalMaaUseMiuixTheme
import top.yukonga.miuix.kmp.basic.Icon as MiuixIcon
import top.yukonga.miuix.kmp.basic.IconButton as MiuixIconButton
import top.yukonga.miuix.kmp.basic.SmallTopAppBar as MiuixSmallTopAppBar
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    navigationIcon: ImageVector? = null,
    onNavigationClick: () -> Unit = {},
    actionIcon: ImageVector? = null,
    actionIconDescription: String? = null,
    onActionClick: () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit)? = null
) {
    val useMiuixTheme = LocalMaaUseMiuixTheme.current

    if (useMiuixTheme) {
        MiuixSmallTopAppBar(
            title = title,
            navigationIcon = {
                navigationIcon?.let { icon ->
                    MiuixIconButton(onClick = onNavigationClick) {
                        MiuixIcon(
                            imageVector = icon,
                            contentDescription = stringResource(R.string.accessibility_navigation),
                            tint = MiuixTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            actions = {
                if (actions != null) {
                    actions()
                } else {
                    actionIcon?.let { icon ->
                        MiuixIconButton(onClick = onActionClick) {
                            MiuixIcon(
                                imageVector = icon,
                                contentDescription = actionIconDescription,
                                tint = MiuixTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        )
        return
    }

    MaterialTopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = {
            navigationIcon?.let { icon ->
                MaterialIconButton(onClick = onNavigationClick) {
                    MaterialIcon(
                        imageVector = icon,
                        contentDescription = stringResource(R.string.accessibility_navigation)
                    )
                }
            }
        },
        actions = {
            // 优先使用自定义 actions
            if (actions != null) {
                actions()
            } else {
                // 兼容旧的单图标模式
                actionIcon?.let { icon ->
                    MaterialIconButton(onClick = onActionClick) {
                        MaterialIcon(
                            imageVector = icon,
                            contentDescription = actionIconDescription
                        )
                    }
                }
            }
        },
        colors = MaterialTopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        )
    )
}
