package com.aliothmoon.maameow.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aliothmoon.maameow.theme.LocalMaaUseMiuixTheme
import com.aliothmoon.maameow.theme.MaaDesignTokens

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
    val actualContainerColor = if (useMiuixTheme) {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
    } else {
        containerColor
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = if (useMiuixTheme) 0.dp else MaaDesignTokens.Card.elevation),
        shape = if (useMiuixTheme) RoundedCornerShape(MaaDesignTokens.CornerRadius.miuixCard) else MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = actualContainerColor
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
