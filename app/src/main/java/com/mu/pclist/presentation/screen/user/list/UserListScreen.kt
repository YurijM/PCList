package com.mu.pclist.presentation.screen.user.list

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
import com.mu.pclist.presentation.navigation.Destinations.UserDestination
import com.mu.pclist.presentation.util.BY_FAMILY
import com.mu.pclist.presentation.util.BY_OFFICES
import com.mu.pclist.presentation.util.BY_SERVICE_NUMBER
import com.mu.pclist.presentation.util.NEW_ID

@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel(),
    toUser: (UserDestination) -> Unit
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
            Title(stringResource(R.string.users) +
                    if (viewModel.users.isNotEmpty()) " (${viewModel.users.size})" else "")
            SortPanel(
                sortList = listOf(BY_FAMILY, BY_SERVICE_NUMBER, BY_OFFICES),
                currentValue = viewModel.sortBy,
                onChange = { newValue -> viewModel.onEvent(UserListEvent.OnUserListSortByChange(newValue)) }
            )
            SearchPanel(
                search = viewModel.search,
                label = "Поиск по ${viewModel.sortBy}",
                onChange = { newValue -> viewModel.onEvent(UserListEvent.OnUserListSearchChange(newValue)) },
                onClear = { viewModel.onEvent(UserListEvent.OnUserListSearchChange("")) },
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
        if (viewModel.foundUsers.isEmpty()) {
            Title(
                title = viewModel.searchResult,
                padding = PaddingValues(top = 60.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(viewModel.foundUsers) { user ->
                    UserListItemScreen(
                        user = user,
                        onEdit = { toUser(UserDestination(user.id)) },
                        onDelete = { viewModel.onEvent(UserListEvent.OnUserListDelete(user)) },
                    )
                }
            }
        }
    }
    FabAdd(
        alignment = Alignment.BottomCenter
    ) { toUser(UserDestination(NEW_ID)) }
}