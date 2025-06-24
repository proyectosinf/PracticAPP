package com.mobivery.fct25.app.common.designsystem.component.menu

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import androidx.compose.foundation.layout.Arrangement

const val NO_SELECTION = -1
private const val DROP_DOWN_ANIMATION_DURATION = 200
private const val DROP_DOWN_CLOSED_ROTATION = 0f
private const val DROP_DOWN_OPENED_ROTATION = 180f

@Composable
fun OutlinedDropDownMenu(
    values: List<String>,
    label: String,
    modifier: Modifier = Modifier,
    selectedIndex: Int = NO_SELECTION,
    enabled: Boolean = true,
    onSelectionChange: (Int) -> Unit = {},
    isError: Boolean = false,
    errorMessage: String = "",
) {
    var expanded by remember { mutableStateOf(false) }

    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) {
            DROP_DOWN_OPENED_ROTATION
        } else {
            DROP_DOWN_CLOSED_ROTATION
        },
        animationSpec = tween(
            durationMillis = DROP_DOWN_ANIMATION_DURATION,
            easing = LinearEasing
        )
    )

    val textFieldColor = if (isError) {
        AppColors.error
    } else {
        AppColors.onSurface
    }

    val textColor = if (selectedIndex == NO_SELECTION) {
        AppColors.outline
    } else {
        AppColors.onSurface
    }

    Box(modifier = modifier) {
        Button(
            onClick = { expanded = true },
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            ),
            shape = RectangleShape,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .alpha(
                    if (enabled) {
                        1f
                    } else {
                        0.5f
                    }
                )
        ) {
            Column {
                OutlinedTextField(
                    value = if (selectedIndex >= 0 && selectedIndex < values.size) {
                        values[selectedIndex]
                    } else {
                        ""
                    },
                    onValueChange = {
                        // Set read-only, so will never change on its own, but onValueChange is mandatory
                    },
                    readOnly = true,
                    enabled = false,
                    label = {
                        Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = textFieldColor,
                        disabledLabelColor = textFieldColor,
                        disabledTrailingIconColor = textFieldColor,
                        unfocusedLeadingIconColor = AppColors.onSurface,
                        disabledTextColor = textColor,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            painterResource(id = R.drawable.disclosures),
                            contentDescription = null,
                            modifier = Modifier.rotate(angle)
                        )
                    },
                    isError = isError,
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
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(AppColors.surface)
        ) {
            values.forEachIndexed { index, value ->
                DropdownMenuItem(
                    text = {
                        Text(value)
                    },
                    onClick = {
                        expanded = false
                        onSelectionChange(index)
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OutlinedDropDownMenuPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(SPACING_M),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            OutlinedDropDownMenu(
                listOf("Element 1", "Element 2"),
                label = "DropDown menu",
                selectedIndex = 0
            )

            OutlinedDropDownMenu(
                listOf("Element 1", "Element 2"),
                label = "DropDown menu",
                selectedIndex = -1
            )
        }
    }
}
