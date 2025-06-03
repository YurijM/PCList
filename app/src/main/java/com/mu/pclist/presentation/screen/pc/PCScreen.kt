package com.mu.pclist.presentation.screen.pc

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.pclist.R
import com.mu.pclist.presentation.component.DropDownList
import com.mu.pclist.presentation.component.OkAndCancel
import com.mu.pclist.presentation.component.OutlinedTextEdit
import com.mu.pclist.presentation.component.Title

@Composable
fun PCScreen(
    viewModel: PCViewModel = hiltViewModel(),
    toPCList: () -> Unit
) {
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
                        title = stringResource(R.string.office),
                        padding = PaddingValues(top = 4.dp)
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    OutlinedTextEdit(
                        label = stringResource(R.string.inventory_number),
                        value = viewModel.pc.inventoryNumber,
                        height = 40.dp,
                        onChange = { value -> viewModel.onEvent(PCEvent.OnPCInventoryNumberChange(value)) },
                        error = viewModel.inventoryNumberError,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    DropDownList(
                        list = viewModel.familyList,
                        label = stringResource(R.string.user),
                        selectedItem = "${viewModel.owner.family} ${viewModel.owner.name} ${viewModel.owner.patronymic}",
                        onClick = { selectedItem -> viewModel.onEvent(PCEvent.OnPCUserChange(selectedItem)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                    )
                    OkAndCancel(
                        titleOk = stringResource(R.string.save),
                        enabledOk = viewModel.inventoryNumberError.isBlank(),
                        onOK = {
                            viewModel.onEvent(PCEvent.OnPCSave)
                            toPCList()
                        },
                        onCancel = { toPCList() },
                    )
                }
            }
        }
    }
}