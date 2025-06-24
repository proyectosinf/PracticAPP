package com.mobivery.fct25.feature.components.ui.componentsmenu

import com.mobivery.fct25.app.common.designsystem.component.buttons.PrimaryButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_L
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.feature.components.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ComponentsMenuScreen(
    navigateToComponentsButtons: () -> Unit,
    navigateToComponentsTextFields: () -> Unit,
    navigateToDialogs: () -> Unit,
    viewModel: ComponentsMenuViewModelInterface = hiltViewModel<ComponentsMenuViewModel>(),
) {
    val uiState by viewModel.componentsMenuUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigation) {
        when (val navigation = uiState.navigation) {
            ComponentsMenuNavigation.NavigateToButtons -> {
                navigateToComponentsButtons()
            }
            ComponentsMenuNavigation.NavigateToTextFields -> {
                navigateToComponentsTextFields()
            }
            ComponentsMenuNavigation.NavigateToDialogs -> {
                navigateToDialogs()
            }
            else -> {
                // Does nothing
            }
        }
        viewModel.handle(ComponentsMenuEvent.DidNavigate)
    }

    Scaffold { padding ->
        Column(
            Modifier
                .padding(top = padding.calculateTopPadding())
                .verticalScroll(rememberScrollState())
                .padding(SPACING_L),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            PrimaryButton(
                text = stringResource(R.string.components_sample_menu_buttons_button),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.handle(ComponentsMenuEvent.ClickOnButtons)
                }
            )
            PrimaryButton(
                text = stringResource(R.string.components_sample_menu_textfields_button),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.handle(ComponentsMenuEvent.ClickOnTextFields)
                }
            )
            PrimaryButton(
                text = stringResource(R.string.components_sample_menu_dialogs_button),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.handle(ComponentsMenuEvent.ClickOnDialogs)
                }
            )
        }
    }
}

@Preview
@Composable
private fun ComponentsMenuScreenPreview() {
    AppTheme {
        ComponentsMenuScreen(
            navigateToComponentsButtons = {},
            navigateToComponentsTextFields = {},
            navigateToDialogs = {},
            viewModel = composePreviewViewModel
        )
    }
}

private val composePreviewViewModel by lazy {
    object : ComponentsMenuViewModelInterface {
        // Outputs
        override val componentsMenuUiState = MutableStateFlow(ComponentsMenuUiState())
        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        // Inputs
        override fun closeError() {}
        override fun handle(event: ComponentsMenuEvent) {}
    }
}