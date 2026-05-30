package com.aliothmoon.maameow.presentation.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton as MaterialFloatingActionButton
import androidx.compose.material3.Icon as MaterialIcon
import androidx.compose.material3.IconButton as MaterialIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold as MaterialScaffold
import androidx.compose.material3.Switch as MaterialSwitch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.aliothmoon.maameow.theme.LocalMaaUseMiuixTheme
import top.yukonga.miuix.kmp.basic.Button as MiuixButton
import top.yukonga.miuix.kmp.basic.Card as MiuixCard
import top.yukonga.miuix.kmp.basic.FloatingActionButton as MiuixFloatingActionButton
import top.yukonga.miuix.kmp.basic.Icon as MiuixIcon
import top.yukonga.miuix.kmp.basic.IconButton as MiuixIconButton
import top.yukonga.miuix.kmp.basic.Scaffold as MiuixScaffold
import top.yukonga.miuix.kmp.basic.Switch as MiuixSwitch
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun AdaptiveScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    if (LocalMaaUseMiuixTheme.current) {
        MiuixScaffold(
            modifier = modifier,
            topBar = topBar,
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
            snackbarHost = snackbarHost,
            content = content
        )
    } else {
        MaterialScaffold(
            modifier = modifier,
            topBar = topBar,
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
            snackbarHost = snackbarHost,
            content = content
        )
    }
}

@Composable
fun AdaptiveIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    if (LocalMaaUseMiuixTheme.current) {
        MiuixIconButton(onClick = onClick, modifier = modifier) {
            MiuixIcon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = MiuixTheme.colorScheme.onSurface
            )
        }
    } else {
        MaterialIconButton(onClick = onClick, modifier = modifier) {
            MaterialIcon(imageVector = imageVector, contentDescription = contentDescription)
        }
    }
}

@Composable
fun AdaptiveSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    if (LocalMaaUseMiuixTheme.current) {
        MiuixSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled
        )
    } else {
        MaterialSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled
        )
    }
}

@Composable
fun AdaptiveCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    if (LocalMaaUseMiuixTheme.current) {
        MiuixCard(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            insideMargin = PaddingValues(0.dp),
            content = content
        )
    } else {
        if (onClick != null) {
            ElevatedCard(
                modifier = modifier.fillMaxWidth(),
                onClick = onClick,
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                content = content
            )
        } else {
            ElevatedCard(
                modifier = modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                content = content
            )
        }
    }
}

@Composable
fun AdaptiveFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (LocalMaaUseMiuixTheme.current) {
        MiuixFloatingActionButton(onClick = onClick, modifier = modifier, content = content)
    } else {
        MaterialFloatingActionButton(onClick = onClick, modifier = modifier, content = content)
    }
}

@Composable
fun AdaptiveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    if (LocalMaaUseMiuixTheme.current) {
        MiuixButton(
            onClick = onClick,
            modifier = modifier.height(44.dp),
            enabled = enabled,
            insideMargin = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
            content = content
        )
    } else {
        androidx.compose.material3.Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            content = content
        )
    }
}
