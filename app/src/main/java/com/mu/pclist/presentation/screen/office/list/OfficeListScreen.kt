package com.mu.pclist.presentation.screen.office.list

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

@Composable
fun OfficeListScreen(
    viewModel: OfficeListViewModel = hiltViewModel(),
) {
    val offices by viewModel.offices.collectAsState(initial = null)

    if (offices == null) {
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
            if (offices!!.isEmpty()) {
                Title(
                    title = "Отделы не заведены",
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
                    Title(stringResource(R.string.offices))
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(offices!!) { office ->
                        OfficeListItemScreen(
                            office = office,
                            onEdit = {},
                            onDelete = {},
                        )
                    }
                }
            }
        }
        FabAdd(
            alignment = Alignment.BottomCenter
        ) {  }
    }
}