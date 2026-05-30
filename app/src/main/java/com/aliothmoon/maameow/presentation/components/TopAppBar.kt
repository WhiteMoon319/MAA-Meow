package com.aliothmoon.maameow.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aliothmoon.maameow.R
import com.aliothmoon.maameow.theme.MaaThemeStyle

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
    val useMiuixTheme = MaaThemeStyle.useMiuixTheme

    if (useMiuixTheme) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                navigationIcon = {
                    navigationIcon?.let { icon ->
                        IconButton(onClick = onNavigationClick) {
                            Icon(
                                imageVector = icon,
                                contentDescription = stringResource(R.string.accessibility_navigation)
                            )
                        }
                    }
                },
                actions = {
                    actions?.invoke(this) ?: actionIcon?.let { icon ->
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.72f),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            IconButton(onClick = onActionClick, modifier = Modifier.height(36.dp)) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = actionIconDescription
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
        return
    }

    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineMedium
            )
        },
        navigationIcon = {
            navigationIcon?.let { icon ->
                IconButton(onClick = onNavigationClick) {
                    Icon(
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
                    IconButton(onClick = onActionClick) {
                        Icon(
                            imageVector = icon,
                            contentDescription = actionIconDescription
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        )
    )
}
