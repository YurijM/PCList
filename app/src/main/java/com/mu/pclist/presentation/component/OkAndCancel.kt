package com.mu.pclist.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mu.pclist.R

@Composable
fun OkAndCancel(
    titleOk: String = stringResource(R.string.ok),
    titleCancel: String = stringResource(R.string.cancel),
    enabledOk: Boolean = false,
    showCancel: Boolean = true,
    onOK: () -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            enabled = enabledOk,
            onClick = { onOK() }
        ) {
            Text(
                text = titleOk,
                style = MaterialTheme.typography.titleMedium
            )
        }
        if (showCancel) {
            Button(
                onClick = { onCancel() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text(
                    text = titleCancel,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}