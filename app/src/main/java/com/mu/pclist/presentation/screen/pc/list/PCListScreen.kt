package com.mu.pclist.presentation.screen.pc.list

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.pclist.R
import com.mu.pclist.presentation.component.FabAdd
import com.mu.pclist.presentation.component.SearchPanel
import com.mu.pclist.presentation.component.SortPanel
import com.mu.pclist.presentation.component.Title
import com.mu.pclist.presentation.navigation.Destinations.PCDestination
import com.mu.pclist.presentation.util.BY_FAMILY
import com.mu.pclist.presentation.util.BY_INVENTORY_NUMBER
import com.mu.pclist.presentation.util.BY_OFFICES
import com.mu.pclist.presentation.util.NEW_ID

@Composable
fun PCListScreen(
    viewModel: PCListViewModel = hiltViewModel(),
    toPC: (PCDestination) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Title(stringResource(R.string.computers) +
                    if (viewModel.computers.isNotEmpty()) " (${viewModel.computers.size})" else "")
            SortPanel(
                sortList = listOf(BY_INVENTORY_NUMBER, BY_FAMILY, BY_OFFICES),
                currentValue = viewModel.sortBy,
                onChange = { newValue -> viewModel.onEvent(PCListEvent.OnPCListSortByChange(newValue)) }
            )
            SearchPanel(
                search = viewModel.search,
                label = "Поиск по ${viewModel.sortBy}",
                onChange = { newValue -> viewModel.onEvent(PCListEvent.OnPCListSearchChange(newValue)) },
                onClear = { viewModel.onEvent(PCListEvent.OnPCListSearchChange("")) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        bottom = 8.dp,
                    )
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        if (viewModel.foundComputers.isEmpty()) {
            Title(
                title = viewModel.searchResult,
                padding = PaddingValues(top = 60.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(viewModel.foundComputers) { pc ->
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
