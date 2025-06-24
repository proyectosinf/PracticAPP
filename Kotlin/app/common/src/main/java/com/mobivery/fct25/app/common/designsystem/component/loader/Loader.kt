package com.mobivery.fct25.app.common.designsystem.component.loader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.base.BaseViewModelInterface
import com.mobivery.fct25.app.common.designsystem.component.dialogs.ErrorDialog
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.LOADING_DIALOG_RADIUS
import com.mobivery.fct25.app.common.designsystem.theme.PROGRESS_BAR_SIZE
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun Loader(
    viewModel: BaseViewModelInterface,
    onDismiss: () -> Unit = { /* Do nothing by default */ },
    circular: Boolean = true,
) {
    val isLoading by viewModel.loading.collectAsState()
    val isError by viewModel.error.collectAsState()
    if (isLoading) {
        if (circular) {
            DialogBoxLoading(onDismiss = onDismiss)
        } else {
            LinearProgressIndicatorLoading()
        }
    }
    isError?.let { error ->
        val errorMessage = localizedAppErrorString(appError = error)
        val title = stringResource(id = R.string.common_error_text)
        ErrorDialog(
            title = title,
            message = errorMessage,
            onButtonClick = {
                viewModel.closeError()
            })
    }
}

@Composable
fun DialogBoxLoading(
    cornerRadius: Dp = LOADING_DIALOG_RADIUS,
    paddingStart: Dp = SPACING_M,
    paddingEnd: Dp = SPACING_M,
    paddingTop: Dp = SPACING_S,
    paddingBottom: Dp = SPACING_S,
    progressIndicatorColor: Color = AppColors.primary,
    progressIndicatorSize: Dp = PROGRESS_BAR_SIZE,
    text: String = stringResource(id = R.string.common_loading_text),
    onDismiss: () -> Unit = { /* Do nothing by default */ },
) {

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(cornerRadius)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        start = paddingStart,
                        end = paddingEnd,
                        top = paddingTop,
                        bottom = paddingBottom
                    ),
            ) {

                CircularProgressIndicatorLoading(
                    progressIndicatorSize = progressIndicatorSize,
                    progressIndicatorColor = progressIndicatorColor
                )

                Spacer(modifier = Modifier.width(SPACING_S))

                Text(
                    text = text,
                    style = AppTypographies.bodyMedium
                )
            }
        }
    }
}

@Composable
fun CircularProgressIndicatorLoading(
    progressIndicatorSize: Dp,
    progressIndicatorColor: Color = AppColors.primary
) {
    CircularProgressIndicator(
        modifier = Modifier.size(progressIndicatorSize),
        color = progressIndicatorColor,
        trackColor = AppColors.onPrimary,
    )
}

@Composable
fun LinearProgressIndicatorLoading(progressIndicatorColor: Color = AppColors.primary) {
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
        color = progressIndicatorColor,
        trackColor = AppColors.onPrimary,
    )
}

@Composable
fun BlockingLoader(
    isLoading: Boolean,
    dismissOnBackPress: Boolean = false,
    dismissOnClickOutside: Boolean = false
) {
    if (!isLoading) return

    Dialog(
        onDismissRequest = {},
        properties = androidx.compose.ui.window.DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
        Surface(
            shape = RoundedCornerShape(LOADING_DIALOG_RADIUS),
            color = AppColors.surface,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(SPACING_M)
            ) {
                CircularProgressIndicator(
                    color = AppColors.primary,
                    trackColor = AppColors.onPrimary,
                    modifier = Modifier.size(PROGRESS_BAR_SIZE)
                )
                Spacer(modifier = Modifier.width(SPACING_S))
                Text(
                    text = stringResource(id = R.string.common_loading_text),
                    style = AppTypographies.bodyMedium,
                    color = AppColors.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun LoaderPreview() {
    Loader(viewModel = composePreviewViewModel)
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun LoaderLinearPreview() {
    Loader(viewModel = composePreviewViewModel, circular = false)
}

@Preview(showBackground = true, widthDp = 300)
@Composable
private fun BlockingLoaderPreview() {
    AppTheme {
        Box(Modifier.fillMaxWidth()) {
            BlockingLoader(isLoading = true)
        }
    }
}

private val composePreviewViewModel by lazy {
    object : BaseViewModelInterface {
        override val loading = MutableStateFlow(true)
        override val error = MutableStateFlow(null)

        override fun closeError() {}
    }
}
