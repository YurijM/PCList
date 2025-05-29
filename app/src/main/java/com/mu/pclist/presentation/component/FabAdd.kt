package com.mu.pclist.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FabAdd(
    alignment: Alignment = Alignment.BottomEnd,
    onAdd: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 8.dp, bottom = 8.dp)
    ) {
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = CircleShape,
            onClick = { onAdd() },
            modifier = Modifier.align(alignment = alignment)
        ) {
            Icon(Icons.Filled.Add, null)
        }
    }
}