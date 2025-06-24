package com.mobivery.fct25.app.common.designsystem.component.textinput

import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XXS
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomTextFieldOutlined(
    value: String,
    label: String? = null,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = stringResource(id = R.string.common_error_text),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = LocalTextStyle.current,
    isEnabled: Boolean = true,
    keyboardSpacing: Dp = 0.dp,
    singleLine: Boolean = true,
    textFieldModifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholder: String? = null,
    onFocusLost: () -> Unit = {},
    onValueChange: (String) -> Unit,
) {
    var hasFocus by remember { mutableStateOf(false) }

    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    val keyboardSeparation = with(LocalDensity.current) {
        WindowInsets.ime.asPaddingValues().calculateBottomPadding().toPx() + keyboardSpacing.toPx()
    }

    var size by remember { mutableStateOf(IntSize.Zero) }

    Column(
        modifier = modifier
            .onSizeChanged { size = it }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = label?.let {
                { Text(text = it) }
            },
            isError = isError,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = AppColors.onSurface,
                focusedBorderColor = AppColors.primary,
                focusedContainerColor = AppColors.surface,
                focusedPlaceholderColor = AppColors.onSurface,
                focusedTrailingIconColor = AppColors.onSurface,
                unfocusedTextColor = AppColors.onSurfaceSecondary,
                unfocusedBorderColor = AppColors.outline,
                unfocusedPlaceholderColor = AppColors.onSurface,
                unfocusedContainerColor = AppColors.surface,
                unfocusedTrailingIconColor = AppColors.onSurfaceSecondary,
                disabledTextColor = AppColors.onSurfaceSecondary,
                disabledBorderColor = AppColors.backgroundVariant2,
                disabledContainerColor = AppColors.backgroundVariant2,
                errorContainerColor = AppColors.surface,
                errorTextColor = AppColors.onSurface,
                errorBorderColor = AppColors.error,
                errorPlaceholderColor = AppColors.error,
                errorTrailingIconColor = AppColors.error,
            ),
            modifier = textFieldModifier
                .fillMaxWidth()
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusEvent {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            bringIntoViewRequester.bringIntoView(
                                Rect(0f, 0f, 0f, keyboardSeparation + size.height)
                            )
                        }
                    }
                }
                .onFocusChanged {
                    if (hasFocus && !it.isFocused) {
                        onFocusLost()
                    }
                    hasFocus = it.isFocused
                },
            textStyle = textStyle,
            enabled = isEnabled,
            singleLine = singleLine,
            trailingIcon = trailingIcon,
            placeholder = placeholder?.let {
                {
                    Text(
                        text = it,
                    )
                }
            }
        )

        if (isError) {
            Text(
                text = errorMessage,
                color = AppColors.error,
                style = AppTypographies.bodySmall,
                modifier = Modifier.padding(start = SPACING_S, top = SPACING_XXS)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomTextFieldOutlinedPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(SPACING_M),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            // Text with text label and user input
            CustomTextFieldOutlined(
                value = "_User input",
                label = "_Text label",
                onValueChange = { /* Do nothing */ }
            )

            // Text with text label
            CustomTextFieldOutlined(
                value = "",
                label = "_Text label",
                onValueChange = { /* Do nothing */ }
            )

            // Text with text placeholder
            CustomTextFieldOutlined(
                value = "",
                placeholder = "_Placeholder text",
                onValueChange = { /* Do nothing */ }
            )

            // Text with error
            CustomTextFieldOutlined(
                value = "_User input",
                label = "_Text label",
                isError = true,
                errorMessage = "_Error message",
                onValueChange = { /* Do nothing */ }
            )

            // Disabled
            CustomTextFieldOutlined(
                value = "",
                placeholder = "_Placeholder",
                isEnabled = false,
                onValueChange = { /* Do nothing */ }
            )

            // Text with right icon
            CustomTextFieldOutlined(
                value = "User input",
                label = "_Text label",
                trailingIcon = {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = null
                    )
                },
                onValueChange = { /* Do nothing */ }
            )
        }
    }
}
