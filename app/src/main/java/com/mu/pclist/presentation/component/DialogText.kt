package com.mu.pclist.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mu.pclist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogText(
    title: String = "",
    text: String = "",
    textAlign: TextAlign = TextAlign.Start,
    titleOK: String = stringResource(R.string.ok),
    titleCancel: String = stringResource(R.string.cancel),
    showCancel: Boolean = false,
    onDismiss: () -> Unit,
    onOK: () -> Unit,
    onCancel: () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
            onDismiss()
        }
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (title.isNotBlank()) {
                    Text(
                        text = title,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = text,
                    textAlign = textAlign
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 12.dp)
                )
                OkAndCancel(
                    titleOk = titleOK,
                    titleCancel = titleCancel,
                    showCancel = showCancel,
                    enabledOk = true,
                    onOK = onOK,
                    onCancel = onCancel
                )
            }
        }
    }
}