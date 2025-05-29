package com.mu.pclist.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Title(
    title: String,
    align: TextAlign = TextAlign.Center,
    color: Color = MaterialTheme.colorScheme.primary,
    padding: PaddingValues = PaddingValues(vertical = 4.dp)
) {
    Text(
        text = title,
        color = color,
        style = MaterialTheme.typography.titleLarge,
        textAlign = align,
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
    )
}