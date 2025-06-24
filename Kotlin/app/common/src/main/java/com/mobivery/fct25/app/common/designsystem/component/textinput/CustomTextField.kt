package com.mobivery.fct25.app.common.designsystem.component.textinput

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XXS
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomTextField(
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
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    hideIndicator: Boolean = false,
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
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = label?.let {
                {
                    Text(
                        text = it,
                        style = AppTypographies.bodySmall.copy(
                            color = if (isError) AppColors.error else AppColors.onSurface
                        )
                    )
                }
            },
            isError = isError,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            colors = if (hideIndicator) {
                TextFieldDefaults.colors(
                    focusedContainerColor = AppColors.surface,
                    unfocusedContainerColor = AppColors.surface,
                    disabledContainerColor = AppColors.surface,
                    errorContainerColor = AppColors.surface,
                    focusedPlaceholderColor = AppColors.onSurface,
                    unfocusedPlaceholderColor = AppColors.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            } else {
                TextFieldDefaults.colors(
                    focusedContainerColor = AppColors.surface,
                    unfocusedContainerColor = AppColors.surface,
                    disabledContainerColor = AppColors.surface,
                    errorContainerColor = AppColors.surface,
                    focusedPlaceholderColor = AppColors.onSurface,
                    unfocusedPlaceholderColor = AppColors.onSurface
                )
            },
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
private fun CustomTextFieldPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(SPACING_M),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            // Text with text label and user input
            CustomTextField(
                value = "_User input",
                label = "_Text label",
                onValueChange = { /* Do nothing */ }
            )

            // Text with text label
            CustomTextField(
                value = "",
                label = "_Text label",
                onValueChange = { /* Do nothing */ }
            )

            // Text with text placeholder
            CustomTextField(
                value = "",
                placeholder = "_Placeholder text",
                onValueChange = { /* Do nothing */ }
            )

            // Text with error
            CustomTextField(
                value = "_User input",
                label = "_Text label",
                isError = true,
                errorMessage = "_Error message",
                onValueChange = { /* Do nothing */ }
            )

            // Disabled
            CustomTextField(
                value = "",
                placeholder = "_Placeholder",
                isEnabled = false,
                onValueChange = { /* Do nothing */ }
            )

            // Text with right icon
            CustomTextField(
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

            // Text large text
            CustomTextField(
                value = LoremIpsum(words = 20).values.first(),
                singleLine = false,
                label = "_Text label",
                onValueChange = { /* Do nothing */ }
            )
        }
    }
}
