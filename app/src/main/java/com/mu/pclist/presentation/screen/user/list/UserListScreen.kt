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
    //val users by viewModel.users.collectAsState(initial = null)

    /*if (users == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {*/
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        //if (users!!.isEmpty()) {
        if (viewModel.users.isEmpty()) {
            Title(
                title = "Пользователи не заведены",
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
                Title(stringResource(R.string.users))
                SortPanel(
                    sortList = listOf(BY_FAMILY, BY_SERVICE_NUMBER, BY_OFFICES),
                    currentValue = viewModel.sortBy,
                    onChange = { newValue -> viewModel.onEvent(UserListEvent.OnUserListSortByChange(newValue)) }
                )
                /*OutlinedTextEdit(
                    value = viewModel.search,
                    shape = ShapeDefaults.Medium,
                    onValueChange = { newValue -> viewModel.onEvent(NoteListEvent.OnNoteSearchChange(newValue)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                        errorLeadingIconColor = MaterialTheme.colorScheme.error,
                    ),
                    label = {
                        Text(
                            text = "Поиск",
                        )
                    },
                    singleLine = true,
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 20.dp, end = 16.dp)
                            )
                            VerticalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .height(dimensionResource(R.dimen.outlined_field_height))
                                    .padding(end = 8.dp)
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { viewModel.onEvent(NoteListEvent.OnNoteSearchChange("")) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                )*/
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                //items(users!!) { user ->
                items(viewModel.users) { user ->
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
    //}
}