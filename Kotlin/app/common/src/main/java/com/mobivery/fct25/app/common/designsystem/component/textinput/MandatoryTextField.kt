package com.mobivery.fct25.app.common.designsystem.component.textinput

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation

/**
 * A TextField component that displays a required indicator when it has never had focus
 * and hides it permanently after gaining focus or when there's an error.
 */
@Composable
fun MandatoryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    baseLabel: String,
    modifier: Modifier = Modifier,
    mandatoryIndicator: String,
    isError: Boolean = false,
    errorMessage: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = LocalTextStyle.current,
    isEnabled: Boolean = true,
    singleLine: Boolean = true,
    placeholder: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onFocusLost: () -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }
    var hadFocus by remember { mutableStateOf(false) }
    val dynamicLabel = if (!hadFocus && !isFocused && value.isEmpty() && !isError) {
        "$baseLabel $mandatoryIndicator"
    } else {
        baseLabel
    }

    CustomTextField(
        value = value,
        onValueChange = onValueChange,
        label = dynamicLabel,
        isError = isError,
        errorMessage = errorMessage,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        textStyle = textStyle,
        isEnabled = isEnabled,
        singleLine = singleLine,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        onFocusLost = onFocusLost,
        modifier = modifier.onFocusChanged { focusState ->
            if (focusState.isFocused) {
                hadFocus = true
            }
            isFocused = focusState.isFocused
        }
    )
}