package com.mobivery.fct25.app.common.designsystem.component.textinput

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S

/**
 * A Password TextField component that displays a required indicator when it does not have focus
 * and hides it when it gains focus or contains text.
 */
@Composable
fun MandatoryPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    baseLabel: String,
    modifier: Modifier = Modifier,
    mandatoryIndicator: String = stringResource(R.string.common_mandatory_placeholder_text),
    isError: Boolean = false,
    errorMessage: String = stringResource(id = R.string.common_error_text),
    isEnabled: Boolean = true,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    placeholder: String? = null,
    onFocusLost: () -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var hadFocus by remember { mutableStateOf(false) }
    val dynamicLabel = if (!hadFocus && !isFocused && value.isEmpty() && !isError) {
        "$baseLabel $mandatoryIndicator"
    } else {
        baseLabel
    }

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
        label = dynamicLabel,
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
        onFocusLost = {
            onFocusLost()
        },
        textFieldModifier = Modifier.onFocusChanged { focusState ->
            if (focusState.isFocused) {
                hadFocus = true
            }
            isFocused = focusState.isFocused
        }
    )
}

@Preview(showBackground = true)
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MandatoryPasswordTextFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(SPACING_M),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            // Empty field (showing mandatory indicator)
            MandatoryPasswordTextField(
                value = "",
                onValueChange = {},
                baseLabel = "Password",
                placeholder = "Enter your password"
            )

            // With input (mandatory indicator should be hidden)
            MandatoryPasswordTextField(
                value = "password123",
                onValueChange = {},
                baseLabel = "Password",
                placeholder = "Enter your password"
            )

            // With error
            MandatoryPasswordTextField(
                value = "pass",
                onValueChange = {},
                baseLabel = "Password",
                isError = true,
                errorMessage = "Password is too short",
                placeholder = "Enter your password"
            )

            // Disabled
            MandatoryPasswordTextField(
                value = "password123",
                onValueChange = {},
                baseLabel = "Password",
                isEnabled = false,
                placeholder = "Enter your password"
            )
        }
    }
}