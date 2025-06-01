package com.mu.pclist.presentation.screen.pc.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.pclist.R
import com.mu.pclist.presentation.component.FabAdd
import com.mu.pclist.presentation.component.Title
import com.mu.pclist.presentation.navigation.Destinations.PCDestination
import com.mu.pclist.presentation.util.NEW_ID

@Composable
fun PCListScreen(
    viewModel: PCListViewModel = hiltViewModel(),
    toPC: (PCDestination) -> Unit
) {
    val computers by viewModel.pcList.collectAsState(initial = null)

    if (computers == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (computers!!.isEmpty()) {
                Title(
                    title = "ПК не заведены",
                    padding = PaddingValues(top = 80.dp)
                )
            } else {
                Card(
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Title(stringResource(R.string.pc_list))
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(computers!!) { pc ->
                        PCListItemScreen(
                            pc = pc,
                            onEdit = { toPC(PCDestination(pc.id)) },
                            onDelete = { viewModel.onEvent(PCListEvent.OnPCListDelete(pc)) },
                        )
                    }
                }
            }
        }
        FabAdd(
            alignment = Alignment.BottomCenter
        ) { toPC(PCDestination(NEW_ID)) }
    }
}