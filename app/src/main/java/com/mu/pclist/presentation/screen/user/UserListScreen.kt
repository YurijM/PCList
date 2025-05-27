package com.mu.pclist.presentation.screen.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mu.pclist.R

@Composable
fun UserListScreen() {
    Text(
        text = stringResource(R.string.user_list),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 96.dp)
    )
}