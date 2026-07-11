package com.aliothmoon.maameow.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.aliothmoon.maameow.R
import com.aliothmoon.maameow.theme.LocalControlOpacity

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
    val containerColor = MaterialTheme.colorScheme.surface.copy(
        alpha = MaterialTheme.colorScheme.surface.alpha * LocalControlOpacity.current
    )
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
            containerColor = containerColor,
            scrolledContainerColor = containerColor,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        )
    )
}
