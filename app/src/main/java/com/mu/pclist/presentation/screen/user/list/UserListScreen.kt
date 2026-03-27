package com.mu.pclist.presentation.screen.user.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mu.pclist.R
import com.mu.pclist.presentation.component.DialogText
import com.mu.pclist.presentation.component.FabAdd
import com.mu.pclist.presentation.component.SearchPanel
import com.mu.pclist.presentation.component.SortPanel
import com.mu.pclist.presentation.component.Title
import com.mu.pclist.presentation.navigation.Destinations.UserDestination
import com.mu.pclist.presentation.util.BY_FAMILY
import com.mu.pclist.presentation.util.BY_OFFICES
import com.mu.pclist.presentation.util.BY_SERVICE_NUMBER
import com.mu.pclist.presentation.util.NEW_ID
import kotlinx.coroutines.launch

@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel(),
    toUser: (UserDestination) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var openDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = viewModel.position) {
        coroutineScope.launch {
            lazyListState.scrollToItem(viewModel.position)
        }
    }

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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(.8f)
                ) {
                    Title(viewModel.title)
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_save),
                    contentDescription = "save",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(8.dp)
                        .clickable { openDialog = true },
                )
            }
            SortPanel(
                sortList = listOf(BY_FAMILY, BY_SERVICE_NUMBER, BY_OFFICES),
                currentValue = viewModel.sortedBy,
                onChange = { newValue -> viewModel.onEvent(UserListEvent.OnUserListSortByChange(newValue)) }
            )
            SearchPanel(
                search = viewModel.search,
                label = "Поиск по ${viewModel.sortedBy}",
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
                state = lazyListState,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(viewModel.foundUsers) { user ->
                    UserListItemScreen(
                        user = user,
                        onEdit = { toUser(UserDestination(user.id, viewModel.sortedBy, viewModel.search)) },
                        onDelete = { viewModel.onEvent(UserListEvent.OnUserListDelete(user)) },
                    )
                }
            }
        }
    }
    FabAdd(
        alignment = Alignment.BottomCenter
    ) { toUser(UserDestination(NEW_ID)) }

    if (openDialog) {
        DialogText(
            text = "Будет создан текстовый файл со списком сотрудиков",
            showCancel = true,
            onDismiss = {},
            titleOK = stringResource(R.string.create),
            titleCancel = stringResource(R.string.no),
            onOK = {
                viewModel.onEvent(UserListEvent.OnUserListDocCreate(context))
                openDialog = false
            },
            onCancel = { openDialog = false },
        )
    }
}