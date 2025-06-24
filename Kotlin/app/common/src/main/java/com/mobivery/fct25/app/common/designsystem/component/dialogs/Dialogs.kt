package com.mobivery.fct25.app.common.designsystem.component.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.component.buttons.TertiaryButton
import com.mobivery.fct25.app.common.designsystem.component.textinput.CustomTextField
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.DEFAULT_DIALOG_ELEVATION
import com.mobivery.fct25.app.common.designsystem.theme.DIALOG_RADIUS
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S

@Composable
fun ErrorDialog(
    title: String,
    message: String,
    onButtonClick: () -> Unit,
    button: String = "",
    onDismiss: () -> Unit = { /* Do nothing by default */ },
) {
    InfoDialog(
        title = title,
        message = message,
        primaryButton = button.ifEmpty { stringResource(id = R.string.common_accept_text) },
        onPrimaryButtonClick = onButtonClick,
        onDismiss = onDismiss,
    )
}

@Composable
fun WarningDialog(
    title: String,
    message: String,
    button: String,
    onButtonClick: () -> Unit,
    onDismiss: () -> Unit = { /* Do nothing by default */ },
) {
    InfoDialog(
        title = title,
        message = message,
        primaryButton = button,
        onPrimaryButtonClick = onButtonClick,
        onDismiss = onDismiss,
    )
}

@Composable
fun InfoDialog(
    title: String,
    message: String,
    primaryButton: String,
    secondaryButton: String? = null,
    onPrimaryButtonClick: () -> Unit,
    onSecondaryButtonClick: () -> Unit = {},
    onDismiss: () -> Unit = { /* Do nothing by default */ },
    primaryButtonColor: Color = AppColors.onSurface,
    secondaryButtonColor: Color = AppColors.onSurfaceSecondary
) {
    AlertDialog(
        shape = RoundedCornerShape(DIALOG_RADIUS),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = AppTypographies.headlineSmall.copy(color = AppColors.onSurface)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(SPACING_S)) {
                Text(
                    text = message,
                    style = AppTypographies.bodyMedium.copy(color = AppColors.onSurface)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    secondaryButton?.let {
                        TertiaryButton(
                            text = it,
                            onClick = onSecondaryButtonClick,
                            textColor = secondaryButtonColor
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TertiaryButton(
                        text = primaryButton,
                        onClick = onPrimaryButtonClick,
                        textColor = primaryButtonColor
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {},
        containerColor = AppColors.surface,
    )
}

@Composable
fun TextInputDialog(
    title: String,
    message: String,
    button: String,
    label: String,
    onClickButton: (String) -> Unit,
    secondaryButton: String? = null,
    onSecondaryButtonClick: () -> Unit = {},
    inputVisualTransformation: VisualTransformation = VisualTransformation.None,
    onDismiss: () -> Unit = { /* Do nothing by default */ },
    textFieldSingleLine: Boolean = true,
    textFieldHeight: Dp? = null,
) {
    var input by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(
            shape = RoundedCornerShape(DIALOG_RADIUS),
            colors = CardDefaults.cardColors(containerColor = AppColors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = DEFAULT_DIALOG_ELEVATION)
        ) {
            Column(
                modifier = Modifier.padding(SPACING_M),
            ) {

                Text(
                    text = title,
                    style = AppTypographies.headlineSmall.copy(color = AppColors.onSurface)
                )
                Spacer(modifier = Modifier.height(SPACING_S))
                Text(
                    text = message,
                    style = AppTypographies.bodyMedium.copy(color = AppColors.onSurface),
                )
                Spacer(modifier = Modifier.height(SPACING_S))
                CustomTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = label,
                    visualTransformation = inputVisualTransformation,
                    singleLine = textFieldSingleLine,
                    textFieldModifier = textFieldHeight?.let { Modifier.height(textFieldHeight) }
                        ?: Modifier
                )
                Spacer(modifier = Modifier.height(SPACING_S))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    secondaryButton?.let {
                        TertiaryButton(
                            text = it,
                            onClick = { onSecondaryButtonClick() },
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    TertiaryButton(
                        text = button,
                        onClick = { onClickButton(input) },
                    )
                }
            }
        }
    }
}

@Composable
fun LogoutDialog(
    title: String,
    message: String,
    onConfirmLogout: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit = {}
) {
    InfoDialog(
        title = title,
        message = message,
        primaryButton = stringResource(R.string.logout_confirm_button).uppercase(),
        secondaryButton = stringResource(R.string.common_cancel_text).uppercase(),
        onPrimaryButtonClick = onConfirmLogout,
        onSecondaryButtonClick = onCancel,
        onDismiss = onDismiss,
        primaryButtonColor = AppColors.error,
        secondaryButtonColor = AppColors.onSurface
    )
}

@Preview
@Composable
private fun ErrorDialogPreview() {
    AppTheme {
        ErrorDialog(title = "_ErrorTitle", message = "_Error message", onButtonClick = {
            // Do nothing
        }
        )
    }
}

@Preview
@Composable
private fun InfoDialogPreview() {
    AppTheme {
        InfoDialog(
            title = "_Info Title",
            message = "_Info message",
            primaryButton = "_Accept button",
            secondaryButton = "_Cancel button",
            onPrimaryButtonClick = {
                // Do nothing
            }
        )
    }
}

@Preview
@Composable
private fun WarningDialogPreview() {
    AppTheme {
        WarningDialog(
            title = "_Warning Title",
            message = "_Warning message",
            button = "_Action button",
            onButtonClick = {}
        )
    }
}

@Preview
@Composable
private fun InputDialogPreview() {
    AppTheme {
        TextInputDialog(
            title = "_Input Title",
            message = "_Input message",
            button = "_Action button",
            label = "_input label",
            onClickButton = {}
        )
    }
}
