package com.mu.pclist.presentation.screen.pc.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.pclist.R
import com.mu.pclist.presentation.navigation.Destinations.PCDestination

@Composable
fun PCListScreen(
    viewModel: PCListViewModel = hiltViewModel(),
    toPC: (PCDestination) -> Unit
) {
    Text(
        text = stringResource(R.string.pc_list),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 96.dp)
    )
}