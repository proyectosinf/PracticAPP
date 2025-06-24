package com.mobivery.fct25.feature.components.ui.componentsdialog

import com.mobivery.fct25.app.common.designsystem.component.buttons.PrimaryButton
import com.mobivery.fct25.app.common.designsystem.component.dialogs.InfoDialog
import com.mobivery.fct25.app.common.designsystem.component.dialogs.ErrorDialog
import com.mobivery.fct25.app.common.designsystem.component.dialogs.WarningDialog
import com.mobivery.fct25.app.common.designsystem.component.dialogs.TextInputDialog
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentsDialogsScreen(
    onBack: () -> Unit,
    viewModel: ComponentsDialogsViewModelInterface = hiltViewModel<ComponentsDialogsViewModel>(),
) {

    val uiState by viewModel.componentsDialogsUiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    LaunchedEffect(uiState.inputText) {
        if (uiState.inputText.isNotBlank()) {
            snackbarHostState.showSnackbar(
                message = context.getString(R.string.components_sample_dialogs_input_text_dialog_snackbar)
                    .replace(
                        "{text}",
                        uiState.inputText
                    ),
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )
            viewModel.handle(ComponentsDialogsEvent.SnackbarDismissed)
        }
    }

    when (uiState.dialog) {
        ComponentsDialogsDialog.InfoDialog -> {
            InfoDialog(
                title = stringResource(R.string.components_sample_dialogs_info_dialog_title_text),
                message = stringResource(R.string.components_sample_dialogs_info_dialog_message_text),
                primaryButton = stringResource(R.string.common_accept_text),
                onPrimaryButtonClick = { viewModel.handle(ComponentsDialogsEvent.DismissDialog) },
                secondaryButton = stringResource(R.string.common_cancel_text),
                onSecondaryButtonClick = { viewModel.handle(ComponentsDialogsEvent.DismissDialog) },
                onDismiss = { viewModel.handle(ComponentsDialogsEvent.DismissDialog) }
            )
        }

        ComponentsDialogsDialog.ErrorDialog -> {
            ErrorDialog(
                title = stringResource(R.string.components_sample_dialogs_error_dialog_title_text),
                message = stringResource(R.string.components_sample_dialogs_error_dialog_message_text),
                button = stringResource(R.string.common_accept_text),
                onButtonClick = { viewModel.handle(ComponentsDialogsEvent.DismissDialog) },
            )
        }

        ComponentsDialogsDialog.WarningDialog -> {
            WarningDialog(
                title = stringResource(R.string.components_sample_dialogs_warning_dialog_title_text),
                message = stringResource(R.string.components_sample_dialogs_warning_dialog_message_text),
                button = stringResource(R.string.common_accept_text),
                onButtonClick = { viewModel.handle(ComponentsDialogsEvent.DismissDialog) },
                onDismiss = { viewModel.handle(ComponentsDialogsEvent.DismissDialog) }
            )
        }

        ComponentsDialogsDialog.TextInputDialog -> {
            TextInputDialog(
                title = stringResource(R.string.components_sample_dialogs_input_text_dialog_title_text),
                message = stringResource(R.string.components_sample_dialogs_input_text_dialog_message_text),
                button = stringResource(R.string.common_accept_text),
                label = stringResource(R.string.components_sample_dialogs_input_text_dialog_placeholder),
                onClickButton = { text ->
                    viewModel.handle(ComponentsDialogsEvent.SetInputText(text))
                },
                secondaryButton = stringResource(R.string.common_cancel_text),
                onSecondaryButtonClick = { viewModel.handle(ComponentsDialogsEvent.DismissDialog) },
            )
        }

        null -> { /* nothing */ }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.components_sample_menu_dialogs_button))
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
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            PrimaryButton(
                text = stringResource(R.string.components_sample_dialogs_info_dialog_title_text),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.handle(ComponentsDialogsEvent.InfoDialog)
                }
            )
            PrimaryButton(
                text = stringResource(R.string.components_sample_dialogs_error_dialog_title_text),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.handle(ComponentsDialogsEvent.ErrorDialog)
                }
            )
            PrimaryButton(
                text = stringResource(R.string.components_sample_dialogs_warning_dialog_title_text),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.handle(ComponentsDialogsEvent.WarningDialog)
                }
            )
            PrimaryButton(
                text = stringResource(R.string.components_sample_dialogs_input_text_dialog_title_text),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.handle(ComponentsDialogsEvent.TextInputDialog)
                }
            )
        }
    }
}

@Preview
@Composable
private fun ComponentsDialogsScreenPreview() {
    AppTheme {
        ComponentsDialogsScreen(
            onBack = {},
            viewModel = composePreviewViewModel
        )
    }
}

private val composePreviewViewModel by lazy {
    object : ComponentsDialogsViewModelInterface {
        // Outputs
        override val componentsDialogsUiState = MutableStateFlow(ComponentsDialogsUiState())
        override val loading = MutableStateFlow(false)
        override val error = MutableStateFlow(null)

        // Inputs
        override fun closeError() {}
        override fun handle(event: ComponentsDialogsEvent) {}
    }
}