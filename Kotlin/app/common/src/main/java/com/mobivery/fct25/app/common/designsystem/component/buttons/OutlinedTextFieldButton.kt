package com.mobivery.fct25.app.common.designsystem.component.buttons

import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedTextFieldButton(
    value: String,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String = "",
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        ),
        shape = RectangleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
    ) {

        val textFieldColor = if (isError) {
            AppColors.error
        } else {
            AppColors.outline
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = value,
                onValueChange = {
                    // Set read-only, so will never change on its own, but onValueChange is mandatory
                },
                readOnly = true,
                enabled = false,
                label = {
                    Text(label)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppColors.primary,
                    unfocusedBorderColor = AppColors.outline,
                    errorBorderColor = AppColors.error,
                    errorLabelColor = AppColors.error,
                    disabledBorderColor = textFieldColor,
                    disabledLabelColor = textFieldColor,
                    disabledTextColor = AppColors.onBackgroundVariant
                ),
                isError = isError,
                leadingIcon = leadingIcon,
                trailingIcon = {
                    Icon(
                        painterResource(id = R.drawable.section_disclosure),
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            if (isError) {
                Text(
                    text = errorMessage,
                    color = AppColors.error,
                    style = AppTypographies.bodySmall,
                    modifier = Modifier.padding(start = SPACING_S)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OutlinedTextFieldButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(SPACING_M),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            OutlinedTextFieldButton(
                value = "",
                label = "empty field",
                onClick = { }
            )
            OutlinedTextFieldButton(
                value = "_Employment guidance",
                label = "label",
                onClick = { }
            )
            OutlinedTextFieldButton(
                value = "_Large text to see what happens when text is more than one line",
                label = "label",
                onClick = { }
            )
            OutlinedTextFieldButton(
                value = "_Field with leading icon",
                label = "label",
                onClick = { },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.logo_icon),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
            OutlinedTextFieldButton(
                value = "_Employment guidance",
                label = "label",
                isError = true,
                onClick = { }
            )
        }
    }
}