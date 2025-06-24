package com.mobivery.fct25.app.common.designsystem.component.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XS
import androidx.compose.foundation.layout.Arrangement

@Composable
fun InfoSetting(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = SPACING_S)
    ) {
        Text(title, style = AppTypographies.titleMedium)
        if (message.isNotEmpty()) {
            Text(message, style = AppTypographies.bodySmall)
        }
    }
}

@Composable
fun ButtonSetting(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    message: String = "",
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
        ),
        contentPadding = PaddingValues(vertical = SPACING_S),
        onClick = onClick,
        shape = RectangleShape,
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = AppTypographies.titleMedium.copy(color = AppColors.primary))
                if (message.isNotEmpty()) {
                    Text(message, style = AppTypographies.bodySmall)
                }
            }
            Icon(
                painter = painterResource(id = R.drawable.section_disclosure),
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}

@Composable
fun SwitchSetting(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = SPACING_XS),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = AppTypographies.titleMedium.copy(color = AppColors.primary), modifier = Modifier.weight(1f))
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(SPACING_M),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            ButtonSetting(title = "_Change password", onClick = {})

            ButtonSetting(title = "_Login data", message = "user@mail.com", onClick = {})

            SwitchSetting(title = "_Remember password", isChecked = false, onCheckedChange = {})

            SwitchSetting(title = "_Remember password", isChecked = true, onCheckedChange = {})

            InfoSetting(title = "_Info title", message = "_Info message")
        }
    }
}