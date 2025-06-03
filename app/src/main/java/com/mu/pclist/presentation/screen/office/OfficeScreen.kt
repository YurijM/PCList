package com.mu.pclist.presentation.screen.office

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mu.pclist.R
import com.mu.pclist.presentation.component.DropDownList
import com.mu.pclist.presentation.component.OkAndCancel
import com.mu.pclist.presentation.component.OutlinedTextEdit
import com.mu.pclist.presentation.component.TextError
import com.mu.pclist.presentation.component.Title

@Composable
fun OfficeScreen(
    viewModel: OfficeViewModel = hiltViewModel(),
    toOfficeList: () -> Unit
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
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        OutlinedTextEdit(
                            label = "Код",
                            value = viewModel.office.code,
                            textAlign = TextAlign.Center,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword,
                            ),
                            height = 40.dp,
                            onChange = { value -> viewModel.onEvent(OfficeEvent.OnOfficeCodeChange(value)) },
                            modifier = Modifier
                                .padding(4.dp)
                                .width(48.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextEdit(
                            label = "Сокращение",
                            value = viewModel.office.shortName,
                            textAlign = TextAlign.Center,
                            height = 40.dp,
                            onChange = { value -> viewModel.onEvent(OfficeEvent.OnOfficeShortNameChange(value)) },
                            modifier = Modifier
                                .width(120.dp)
                        )
                    }
                    var error = ""
                    if (viewModel.codeError.isNotBlank()) error = viewModel.codeError
                    else if (viewModel.shortNameError.isNotBlank()) error = viewModel.shortNameError
                    if (error.isNotBlank()) {
                        TextError(
                            error = error,
                            textAlign = TextAlign.Center
                        )
                    }
                    OutlinedTextEdit(
                        label = "Отдел",
                        value = viewModel.office.office,
                        height = 48.dp,
                        singleLine = false,
                        onChange = { value -> viewModel.onEvent(OfficeEvent.OnOfficeOfficeChange(value)) },
                        error = viewModel.officeError,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    DropDownList(
                        list = viewModel.familyList,
                        label = stringResource(R.string.chief),
                        selectedItem = "${viewModel.chief.family} ${viewModel.chief.name} ${viewModel.chief.patronymic}",
                        onClick = { selectedItem -> viewModel.onEvent(OfficeEvent.OnOfficeChiefChange(selectedItem)) },
                        modifier = Modifier
                            .fillMaxWidth()
                        //.padding(8.dp)
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                    )
                    OkAndCancel(
                        titleOk = stringResource(R.string.save),
                        enabledOk = viewModel.enabled,
                        onOK = {
                            viewModel.onEvent(OfficeEvent.OnOfficeSave)
                            toOfficeList()
                        },
                        onCancel = { toOfficeList() },
                    )
                }
            }
        }
    }
}