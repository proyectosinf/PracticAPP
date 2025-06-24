package com.mobivery.fct25.app.common.designsystem.component.textinput

import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S

private const val MAX_SUGGESTIONS_DISPLAYED = 5

/**
 * Example of use
 * val options = listOf("example", "examiner", "examination")
 *
 * var textFieldValue by remember { mutableStateOf(TextFieldValue()) }
 *
 * TextFieldSuggestion(
 *      modifier = Modifier.fillMaxWidth(),
 *      value = textFieldValue,
 *      onValueChange = { value ->
 *          textFieldValue = value
 *      },
 *      options = options,
 *      label = "Label"
 * )
 */
@Composable
fun TextFieldWithSuggestion(
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    modifier: Modifier = Modifier,
    label: String = "",
    isError: Boolean = false,
    errorMessage: String = stringResource(id = R.string.common_error_text),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onSuggestionSelected: (String) -> Unit = {},
    onFocusLost: () -> Unit = {},
) {
    val interactionSource =
        remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    Box(modifier) {
        CustomTextField(
            value = value,
            label = label,
            onValueChange = onValueChange,
            isError = isError,
            errorMessage = errorMessage,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            onFocusLost = onFocusLost,
            interactionSource = interactionSource
        )

        val filteredOptions = remember(value, options) {
            options
                .filter { it.contains(value, ignoreCase = true) && !it.equals(value, ignoreCase = true) }
                .take(MAX_SUGGESTIONS_DISPLAYED)
        }

        DropdownMenu(
            expanded = isFocused && filteredOptions.isNotEmpty(),
            onDismissRequest = { /* nothing */ },
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            filteredOptions.forEach { text ->
                DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        onValueChange(text)
                        onSuggestionSelected(text)
                    }
                )
            }
        }
    }
}

data class TextFieldWithSuggestionState(
    val text: String,
    val isError: Boolean = false,
    val suggestions: List<String> = emptyList(),
)

@Preview(showBackground = true, widthDp = 800)
@Composable
private fun TextFieldWithSuggestionsPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(100.dp),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            TextFieldWithSuggestion(
                value = "_User input",
                label = "_Text label",
                onValueChange = { /* Do nothing */ },
                options = listOf("example", "examiner", "examination")
            )
        }
    }
}
