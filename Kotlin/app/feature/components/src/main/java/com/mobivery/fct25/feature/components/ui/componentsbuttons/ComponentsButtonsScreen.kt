package com.mobivery.fct25.feature.components.ui.componentsbuttons

import com.mobivery.fct25.app.common.designsystem.component.buttons.LinkButton
import com.mobivery.fct25.app.common.designsystem.component.buttons.OutlinedTextFieldButton
import com.mobivery.fct25.app.common.designsystem.component.buttons.PrimaryButton
import com.mobivery.fct25.app.common.designsystem.component.buttons.SecondaryButton
import com.mobivery.fct25.app.common.designsystem.component.buttons.TertiaryButton
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_L
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.feature.components.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentsButtonsScreen(
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.components_sample_menu_buttons_button))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null
                        )
                    }
                },
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(top = padding.calculateTopPadding())
                .verticalScroll(rememberScrollState())
                .padding(SPACING_L),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(SPACING_L)
        ) {
            PrimaryButtons()

            SecondaryButtons()

            TertiaryButtons()

            OutlinedButtons()

            LinkButton(
                text = stringResource(R.string.components_sample_buttons_enabled_button),
                modifier = Modifier.fillMaxWidth(),
                onClick = { }
            )
        }
    }
}

@Composable
private fun PrimaryButtons() {
    Column (
        verticalArrangement = Arrangement.spacedBy(SPACING_S)
    ) {
        Text(stringResource(R.string.components_sample_buttons_primary_text), style = AppTypographies.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            PrimaryButton(
                text = stringResource(R.string.components_sample_buttons_enabled_button),
                modifier = Modifier.weight(1f),
                onClick = { }
            )
            PrimaryButton(
                text = stringResource(R.string.components_sample_buttons_disabled_button),
                modifier = Modifier.weight(1f),
                enabled = false,
                onClick = { }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            PrimaryButton(
                text = stringResource(R.string.components_sample_buttons_enabled_button),
                modifier = Modifier.weight(1f),
                iconResource = com.mobivery.fct25.app.common.R.drawable.ic_arrow_back,
                onClick = { }
            )
            PrimaryButton(
                text = stringResource(R.string.components_sample_buttons_disabled_button),
                modifier = Modifier.weight(1f),
                iconResource = com.mobivery.fct25.app.common.R.drawable.ic_arrow_back,
                enabled = false,
                onClick = { }
            )
        }
    }
}

@Composable
private fun SecondaryButtons() {
    Column (
        verticalArrangement = Arrangement.spacedBy(SPACING_S)
    ) {
        Text(stringResource(R.string.components_sample_buttons_secondary_text), style = AppTypographies.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            SecondaryButton(
                text = stringResource(R.string.components_sample_buttons_enabled_button),
                modifier = Modifier.weight(1f),
                onClick = { }
            )
            SecondaryButton(
                text = stringResource(R.string.components_sample_buttons_disabled_button),
                modifier = Modifier.weight(1f),
                enabled = false,
                onClick = { }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            SecondaryButton(
                text = stringResource(R.string.components_sample_buttons_enabled_button),
                modifier = Modifier.weight(1f),
                iconResource = com.mobivery.fct25.app.common.R.drawable.ic_arrow_back,
                onClick = { }
            )
            SecondaryButton(
                text = stringResource(R.string.components_sample_buttons_disabled_button),
                modifier = Modifier.weight(1f),
                iconResource = com.mobivery.fct25.app.common.R.drawable.ic_arrow_back,
                enabled = false,
                onClick = { }
            )
        }
    }
}

@Composable
private fun TertiaryButtons() {
    Column (
        verticalArrangement = Arrangement.spacedBy(SPACING_S)
    ) {
        Text(stringResource(R.string.components_sample_buttons_tertiary_text), style = AppTypographies.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            TertiaryButton(
                text = stringResource(R.string.components_sample_buttons_enabled_button),
                modifier = Modifier.weight(1f),
                onClick = { }
            )
            TertiaryButton(
                text = stringResource(R.string.components_sample_buttons_enabled_button),
                modifier = Modifier.weight(1f),
                onClick = { }
            )
        }
    }
}

@Composable
private fun OutlinedButtons() {
    Column (
        verticalArrangement = Arrangement.spacedBy(SPACING_S)
    ) {
        Text(stringResource(R.string.components_sample_buttons_outlined_text), style = AppTypographies.titleMedium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            OutlinedTextFieldButton(
                value = "",
                label = stringResource(R.string.components_sample_buttons_enabled_button),
                modifier = Modifier.weight(1f),
                onClick = { }
            )
            OutlinedTextFieldButton(
                value = stringResource(R.string.components_sample_buttons_enabled_button),
                label = stringResource(R.string.components_sample_buttons_enabled_button),
                modifier = Modifier.weight(1f),
                onClick = { },
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            OutlinedTextFieldButton(
                value = stringResource(R.string.components_sample_buttons_enabled_button),
                label = stringResource(R.string.components_sample_buttons_enabled_button),
                modifier = Modifier.weight(1f),
                onClick = { },
                leadingIcon = {
                    Icon(Icons.Default.Build, contentDescription = null)
                }
            )
            OutlinedTextFieldButton(
                value = stringResource(R.string.components_sample_buttons_enabled_button),
                label = stringResource(R.string.components_sample_buttons_enabled_button),
                modifier = Modifier.weight(1f),
                onClick = { },
                errorMessage = stringResource(R.string.common_error_text),
                isError = true
            )
        }
    }
}

@Preview
@Composable
private fun ComponentsButtonsScreenPreview() {
    AppTheme {
        ComponentsButtonsScreen(
            onBack = {}
        )
    }
}