package com.mu.pclist.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp

@Composable
fun SortPanel(
    sortList: List<String>,
    currentValue: String,
    onChange: (String) -> Unit
) {
    var heightCard by remember { mutableIntStateOf(0) }

    Box {
        Card(
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
                //containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 12.dp,
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 8.dp,
                )
                .onSizeChanged { size ->
                    heightCard = size.height
                }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup()
                    .padding(
                        top = 12.dp,
                        bottom = 8.dp
                    )
            ) {
                RadioGroup(
                    items = sortList,
                    currentValue = currentValue,
                    onClick = { newValue -> onChange(newValue) }
                )
                /*Text(
                    text = "фамилии",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = "таб.номеру",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = "отделу",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )*/
            }
        }
        Text(
            text = " Сортировка по ",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                //.offset(x = 32.dp, y = (-heightCard / 2 - 4).dp)
                .padding(start = 30.dp)
                .background(color = MaterialTheme.colorScheme.background)
                .padding(horizontal = 4.dp)
        )
    }
}