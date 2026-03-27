package com.mu.pclist.presentation.screen.office.list

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.mu.pclist.presentation.component.Title
import com.mu.pclist.presentation.navigation.Destinations.OfficeDestination
import com.mu.pclist.presentation.util.NEW_ID
import com.mu.pclist.presentation.util.OFFICE_LIST_IS_EMPTY

@Composable
fun OfficeListScreen(
    viewModel: OfficeListViewModel = hiltViewModel(),
    toOffice: (OfficeDestination) -> Unit
) {
    //val offices by viewModel.offices.collectAsState(initial = null)
    val context = LocalContext.current
    var openDialog by remember { mutableStateOf(false) }

    /*if (offices == null) {
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
                    .padding(5.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(.8f)
                ) {
                    Title(
                        stringResource(R.string.offices) +
                                //if (offices!!.isNotEmpty()) " (${offices!!.size})" else ""
                                if (viewModel.offices.isNotEmpty()) " (${viewModel.offices.size})" else ""
                    )
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
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        //if (offices!!.isEmpty()) {
        if (viewModel.offices.isEmpty()) {
            Title(
                title = OFFICE_LIST_IS_EMPTY,
                padding = PaddingValues(top = 60.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(viewModel.offices) { office ->
                    OfficeListItemScreen(
                        office = office,
                        onEdit = { toOffice(OfficeDestination(office.id)) },
                        onDelete = { viewModel.onEvent(OfficeListEvent.OnOfficeListDelete(office)) },
                    )
                }
            }
        }
    }
    FabAdd(
        alignment = Alignment.BottomCenter
    ) { toOffice(OfficeDestination(NEW_ID)) }

    if (openDialog) {
        DialogText(
            text = "Будет создан текстовый файл со списком отделов",
            showCancel = true,
            onDismiss = {},
            titleOK = stringResource(R.string.create),
            titleCancel = stringResource(R.string.no),
            onOK = {
                viewModel.onEvent(OfficeListEvent.OnOfficeListDocCreate(context))
                openDialog = false
            },
            onCancel = { openDialog = false },
        )
    }
    //}
}