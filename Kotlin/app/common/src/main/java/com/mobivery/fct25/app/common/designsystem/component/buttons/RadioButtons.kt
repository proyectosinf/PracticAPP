package com.mobivery.fct25.app.common.designsystem.component.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.HEIGHT_RADIUS_BUTTON
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S

@Composable
fun RadioButtonWithCheck(
    text: String,
    selected: Boolean,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onSelected: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.RadioButton
            ),
    ) {
        Text(
            text,
            style = AppTypographies.bodyLarge,
        )
        Spacer(Modifier.weight(1f))
        IconToggleButton(
            checked = selected,
            onCheckedChange = {
                if (it) {
                    onSelected()
                }
            },
            modifier = Modifier.size(HEIGHT_RADIUS_BUTTON)
        ) {
            Icon(
                imageVector = if (selected) {
                    Icons.Filled.RadioButtonChecked
                } else {
                    Icons.Filled.RadioButtonUnchecked
                },
                tint = if (selected) AppColors.primary else AppColors.onBackgroundSecondary,
                contentDescription = contentDescription,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RadioButtonListPreview() {
    var selectedOption by remember { mutableIntStateOf(1) }

    AppTheme {
        Column(
            modifier = Modifier.padding(SPACING_M),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            RadioButtonWithCheck(
                text = "_Radio Button 1",
                selected = (selectedOption == 1),
                contentDescription = "_Radio button for option 1",
                onSelected = { selectedOption = 1 }
            )
            RadioButtonWithCheck(
                text = "_Radio Button 2",
                selected = (selectedOption == 2),
                contentDescription = "_Radio button for option 2",
                onSelected = { selectedOption = 2 }
            )
        }
    }
}