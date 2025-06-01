package com.mu.pclist.presentation.screen.pc

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PCScreen(
    viewModel: PCViewModel = hiltViewModel(),
    toPCList: () -> Unit
) {
}