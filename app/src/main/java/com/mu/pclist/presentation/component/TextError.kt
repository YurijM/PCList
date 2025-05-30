package com.mu.pclist.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TextError(
    error: String,
    textAlign: TextAlign? = null,
) {
    Text(
        modifier = Modifier
            //.fillMaxWidth()
            .padding(4.dp),
        text = error,
        //color = MaterialTheme.colorScheme.error,
        color = Color.Blue,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.bodySmall,
        textAlign = textAlign
    )
}