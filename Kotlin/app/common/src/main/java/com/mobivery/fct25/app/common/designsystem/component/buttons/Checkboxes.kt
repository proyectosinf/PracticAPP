package com.mobivery.fct25.app.common.designsystem.component.buttons

import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.HEIGHT_RADIUS_BUTTON
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SquareCheckboxWithCheckCustomIcon(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .selectable(
                selected = checked,
                onClick = { onCheckedChange(!checked) },
                role = Role.Checkbox
            )
    ) {
        Text(
            text = text,
            style = AppTypographies.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))

        IconToggleButton(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier
                .size(HEIGHT_RADIUS_BUTTON)
                .semantics { this.contentDescription = contentDescription }
        ) {
            Icon(
                imageVector = if (checked) {
                    Icons.Filled.CheckBox
                } else {
                    Icons.Filled.CheckBoxOutlineBlank
                },
                tint = if (checked) AppColors.primary else AppColors.onBackgroundSecondary,
                contentDescription = contentDescription
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CheckboxesPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(SPACING_M),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            SquareCheckboxWithCheckCustomIcon(
                text = "_Checked Checkbox",
                checked = true,
                onCheckedChange = { /* Do nothing */ },
                contentDescription = "_Checked item"
            )
            SquareCheckboxWithCheckCustomIcon(
                text = "_Unchecked Checkbox",
                checked = false,
                onCheckedChange = { /* Do nothing */ },
                contentDescription = "_Unchecked item"
            )
        }
    }
}
