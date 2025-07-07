package com.mu.pclist.presentation.screen.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.pclist.R
import com.mu.pclist.presentation.component.DropDownList
import com.mu.pclist.presentation.component.OkAndCancel
import com.mu.pclist.presentation.component.OutlinedTextEdit
import com.mu.pclist.presentation.component.Title
import com.mu.pclist.presentation.navigation.Destinations

@Composable
fun UserScreen(
    viewModel: UserViewModel = hiltViewModel(),
    toUserList: (Destinations.UserListDestination) -> Unit
) {
    LaunchedEffect(key1 = viewModel.saved) {
        if (viewModel.saved) toUserList(Destinations.UserListDestination(viewModel.user.id))
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(.85f),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.background
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Title(
                        title = stringResource(R.string.user),
                        padding = PaddingValues(top = 4.dp)
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    OutlinedTextEdit(
                        label = stringResource(R.string.family),
                        value = viewModel.user.family,
                        height = 40.dp,
                        onChange = { value -> viewModel.onEvent(UserEvent.OnUserFamilyChange(value)) },
                        error = viewModel.familyError,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextEdit(
                        label = stringResource(R.string.name),
                        value = viewModel.user.name,
                        height = 40.dp,
                        onChange = { value -> viewModel.onEvent(UserEvent.OnUserNameChange(value)) },
                        error = viewModel.nameError,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextEdit(
                        label = stringResource(R.string.patronymic),
                        value = viewModel.user.patronymic,
                        height = 40.dp,
                        onChange = { value -> viewModel.onEvent(UserEvent.OnUserPatronymicChange(value)) },
                        error = viewModel.patronymicError,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    Row {
                        OutlinedTextEdit(
                            label = stringResource(R.string.service_number),
                            value = viewModel.user.serviceNumber,
                            height = 40.dp,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                            ),
                            onChange = { value -> viewModel.onEvent(UserEvent.OnUserServiceNumberChange(value)) },
                            error = viewModel.serviceNumberError,
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        )
                        OutlinedTextEdit(
                            label = stringResource(R.string.phone),
                            value = viewModel.user.phone,
                            height = 40.dp,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                            ),
                            onChange = { value -> viewModel.onEvent(UserEvent.OnUserPhoneChange(value)) },
                            error = viewModel.phoneError,
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                        )
                    }
                    DropDownList(
                        list = viewModel.officeList,
                        label = stringResource(R.string.office),
                        selectedItem = viewModel.office.shortName,
                        onClick = { selectedItem -> viewModel.onEvent(UserEvent.OnUserOfficeChange(selectedItem)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                    )
                    OkAndCancel(
                        titleOk = stringResource(R.string.save),
                        enabledOk = viewModel.enabled,
                        onOK = {
                            viewModel.onEvent(UserEvent.OnUserSave)
                            //toUserList()
                        },
                        onCancel = { toUserList(Destinations.UserListDestination(viewModel.user.id)) },
                    )
                }
            }
        }
    }
}