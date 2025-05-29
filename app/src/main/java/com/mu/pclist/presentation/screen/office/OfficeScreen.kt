package com.mu.pclist.presentation.screen.office

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OfficeScreen(
    viewModel: OfficeViewModel = hiltViewModel(),
    toOfficeList: () -> Unit
) {
    Text(
        text = viewModel.office.toString(),
        textAlign = TextAlign.Center
    )
}