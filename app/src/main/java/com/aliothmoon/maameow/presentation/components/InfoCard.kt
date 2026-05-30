package com.aliothmoon.maameow.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card as MaterialCard
import androidx.compose.material3.CardDefaults as MaterialCardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aliothmoon.maameow.theme.LocalMaaUseMiuixTheme
import com.aliothmoon.maameow.theme.MaaDesignTokens
import top.yukonga.miuix.kmp.basic.Card as MiuixCard
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun InfoCard(
    title: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    contentPadding: PaddingValues = PaddingValues(MaaDesignTokens.Card.innerPadding),
    content: @Composable ColumnScope.() -> Unit
) {
    val useMiuixTheme = LocalMaaUseMiuixTheme.current

    if (useMiuixTheme) {
        MiuixCard(
            modifier = modifier.fillMaxWidth(),
            insideMargin = PaddingValues(vertical = 0.dp)
        ) {
            if (title.isNotEmpty()) {
                top.yukonga.miuix.kmp.basic.Text(
                    text = title,
                    style = MiuixTheme.textStyles.subtitle,
                    color = MiuixTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = MaaDesignTokens.Spacing.sm)
                )
            }
            content()
        }
        return
    }

    MaterialCard(
        modifier = modifier.fillMaxWidth(),
        elevation = MaterialCardDefaults.cardElevation(defaultElevation = MaaDesignTokens.Card.elevation),
        shape = MaterialTheme.shapes.medium,
        colors = MaterialCardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
        ) {
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
                    modifier = Modifier.padding(bottom = MaaDesignTokens.Spacing.sm)
                )
            }
            content()
        }
    }
}
