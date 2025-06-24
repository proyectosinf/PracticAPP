package com.mobivery.fct25.app.common.designsystem.component.textinput

import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = stringResource(id = R.string.common_error_text),
    isEnabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    placeholder: String? = null,
    onFocusLost: (() -> Unit) = {},
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val icon = if (passwordVisible) {
        Icons.Filled.Visibility
    } else {
        Icons.Filled.VisibilityOff
    }

    val iconContentDescription = stringResource(
        id = if (passwordVisible) {
            R.string.common_hide_password_a11y
        } else {
            R.string.common_show_password_a11y
        }
    )

    val trailingIcon: @Composable (() -> Unit) = {
        IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(
                imageVector = icon,
                contentDescription = iconContentDescription
            )
        }
    }

    CustomTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        isError = isError,
        errorMessage = errorMessage,
        isEnabled = isEnabled,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions.copy(
            keyboardType = KeyboardType.Password,
        ),
        keyboardActions = keyboardActions,
        onFocusLost = { onFocusLost() }
    )
}

@Preview(showBackground = true)
@Composable
private fun PasswordTextFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(SPACING_M),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
           // Placeholder
            PasswordTextField(
                value = "",
                onValueChange = {},
                label = "Password",
                placeholder = "Enter your password",
                onFocusLost = {}
            )

            // Placeholder with input
            PasswordTextField(
                value = "password123",
                onValueChange = {},
                label = "Password",
                placeholder = "Enter your password",
                onFocusLost = {}
            )

            // With error
            PasswordTextField(
                value = "pass",
                onValueChange = {},
                label = "Password",
                isError = true,
                errorMessage = "Password is too short",
                placeholder = "Enter your password",
                onFocusLost = {}
            )

            // Disabled
            PasswordTextField(
                value = "password123",
                onValueChange = {},
                label = "Password",
                isEnabled = false,
                placeholder = "Enter your password",
                onFocusLost = {}
            )
        }
    }
}
