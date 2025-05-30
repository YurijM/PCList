package com.mu.pclist.presentation.component

import androidx.compose.ui.graphics.Shape
import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.contentPaddingWithLabel
import androidx.compose.material3.TextFieldDefaults.contentPaddingWithoutLabel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.mu.pclist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextEdit(
    modifier: Modifier = Modifier,
    value: String = "",
    onChange: (String) -> Unit,
    fontSize: TextUnit = MaterialTheme.typography.bodyLarge.fontSize,
    height: Dp = dimensionResource(R.dimen.outlined_field_height),
    textAlign: TextAlign = TextAlign.Unspecified,
    label: String = "",
    @DrawableRes painterId: Int? = null,
    description: String = "",
    enabled: Boolean = true,
    error: String = "",
    singleLine: Boolean = true,
    shape: Shape = ShapeDefaults.Small,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
    ) {
        BasicTextField(
            enabled = enabled,
            value = value,
            onValueChange = { onChange(it) },
            interactionSource = interactionSource,
            singleLine = singleLine,
            textStyle = TextStyle(
                fontSize = fontSize,
                textAlign = textAlign
            ),
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
            //.width(OutlinedTextFieldDefaults.MinWidth)
            //.padding(8.dp),
        ) { innerTextField ->
            OutlinedTextFieldDefaults.DecorationBox(
                value = value,
                label = {
                    if (label.isNotBlank()) {
                        Text(label)
                    }
                },
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = singleLine,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                leadingIcon = if (painterId != null) {
                    {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            painter = painterResource(id = painterId),
                            contentDescription = description
                        )
                    }
                } else null,
                contentPadding = if (label.isBlank())
                    contentPaddingWithoutLabel(
                        top = 0.dp,
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 0.dp
                    )
                else
                    contentPaddingWithLabel(
                        top = 0.dp,
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 0.dp
                    ),
                container = {
                    OutlinedTextFieldDefaults.Container(
                        enabled = enabled,
                        isError = error.isNotBlank(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                            errorLeadingIconColor = MaterialTheme.colorScheme.error,
                        ),
                        interactionSource = interactionSource,
                        shape = shape,
                    )
                }
            )
        }
        if (error.isNotBlank()) {
            TextError(error)
        }
    }
}