package com.mu.pclist.presentation.screen.user.list

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.mu.pclist.R
import com.mu.pclist.domain.model.UserModel
import com.mu.pclist.presentation.component.DialogText

@Composable
fun UserListItemScreen(
    user: UserModel,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    var openDialog by remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            )
            .clickable(
                role = Role.Button,
                onClick = { onEdit() }
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = "${user.family} ${user.name} ${user.patronymic}",
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 1.em
                )
                Row {
                    Text(
                        text = "таб. № ${user.serviceNumber}",
                        lineHeight = 1.em
                    )
                    if (user.inventoryNumber.isNotEmpty()) {
                        Text(
                            text = ",",
                            lineHeight = 1.em,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "ПК инв. № ${user.inventoryNumber}.",
                            lineHeight = 1.em
                        )
                    }
                }
                if (user.officeId > 0) {
                    Text(
                        text = "отдел ${user.office}",
                        style = MaterialTheme.typography.titleSmall,
                        fontStyle = FontStyle.Italic,
                    )
                }
            }
            IconButton(
                onClick = { openDialog = true },
                modifier = Modifier.weight(.15f)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(8.dp)
                )
            }
        }
    }
    if (openDialog) {
        DialogText(
            text = "Вы действительно хотите удалить пользователя \"${user.family} ${user.name} ${user.patronymic}\" ?",
            showCancel = true,
            onDismiss = {},
            titleOK = stringResource(R.string.yes),
            titleCancel = stringResource(R.string.no),
            onOK = {
                onDelete()
                openDialog = false
            },
            onCancel = { openDialog = false },
        )
    }
}