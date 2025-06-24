package com.mobivery.fct25.app.common.designsystem.component.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTypography
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S

private val DropdownCornerRadius: Dp = 12.dp

@Composable
fun <T> DropdownSelector(
    label: String,
    options: List<T>,
    selectedOption: T?,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(DropdownCornerRadius),
    onOptionSelected: (T) -> Unit,
    labelMapper: @Composable (T) -> String,
    isError: Boolean = false,
    errorMessage: String = "",
    onFocusLost: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    val displayText = selectedOption?.let { labelMapper(it) }
        ?: stringResource(id = R.string.offer_select_text)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = AppTypography.bodyLarge,
            color = AppColors.onSurface,
            modifier = Modifier.padding(bottom = SPACING_S)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(Color.Transparent)
                .border(
                    width = 1.dp,
                    color = if (isError) AppColors.error else AppColors.outline,
                    shape = shape
                )
                .clickable {
                    expanded = true
                }
                .padding(horizontal = SPACING_M, vertical = SPACING_S)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayText,
                    style = AppTypography.bodyMedium,
                    color = if (isError) AppColors.error else AppColors.onSurface,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = AppColors.onSurfaceSecondary
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                    onFocusLost()
                },
                modifier = Modifier
                    .background(AppColors.surface)
                    .padding(SPACING_S)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        text = {
                            Text(
                                text = labelMapper(option),
                                style = AppTypography.bodyMedium,
                                color = AppColors.onSurface,
                                modifier = Modifier.padding(SPACING_S)
                            )
                        }
                    )
                }
            }
        }

        if (isError) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                style = AppTypography.labelSmall,
                color = AppColors.error,
                modifier = Modifier.padding(start = SPACING_M)
            )
        }
    }
}
