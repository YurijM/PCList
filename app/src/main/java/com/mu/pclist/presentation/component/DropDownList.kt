package com.mu.pclist.presentation.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.contentPaddingWithLabel
import androidx.compose.material3.TextFieldDefaults.contentPaddingWithoutLabel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mu.pclist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownList(
    modifier: Modifier = Modifier,
    list: List<String>,
    label: String,
    selectedItem: String,
    textAlign: TextAlign = TextAlign.Start,
    onClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        BasicTextField(
            value = selectedItem,
            readOnly = true,
            onValueChange = {},
            interactionSource = interactionSource,
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                textAlign = textAlign
            ),
            modifier = modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .height(dimensionResource(R.dimen.outlined_field_height))
                .padding(8.dp),
        ) { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = selectedItem,
                label = {
                    if (label.isNotBlank()) {
                        Text(
                            text = label,
                            //textAlign = TextAlign.Center
                        )
                    }
                },
                innerTextField = innerTextField,
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                trailingIcon =  {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.rotate(if (expanded) 180f else 0f)
                    )
                },
                contentPadding = if (label.isBlank())
                    contentPaddingWithoutLabel(
                        top = 0.dp,
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 0.dp
                    )
                else
                    contentPaddingWithLabel(
                        top = 8.dp,
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 0.dp
                    ),
                container = {
                    OutlinedTextFieldDefaults.Container(
                        enabled = true,
                        isError = false,
                        interactionSource = interactionSource,
                        shape = ShapeDefaults.Small,
                    )
                }
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            list.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier.height(32.dp),
                    onClick = {
                        onClick(item)
                        expanded = false
                    },
                    text = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = item,
                            textAlign = textAlign
                        )
                    }
                )
            }
        }
    }
}