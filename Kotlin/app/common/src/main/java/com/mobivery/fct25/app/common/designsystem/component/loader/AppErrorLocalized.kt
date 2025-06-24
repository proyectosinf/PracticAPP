package com.mobivery.fct25.app.common.designsystem.component.loader

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.domain.model.error.AppError

@Composable
fun localizedAppErrorString(appError: AppError) = when (appError) {
    is AppError.ApiError -> appError.serverMessage
        ?: stringResource(R.string.common_default_server_error_text)
    // TODO: show the properly messages for the below errors
    is AppError.InternalError -> stringResource(R.string.common_default_server_error_text)
    is AppError.Unknown -> stringResource(R.string.common_default_server_error_text)
    is AppError.Login -> stringResource(R.string.login_error_credentials_text)
    is AppError.UnauthorizedError -> stringResource(R.string.common_unauthorized_error_text)
}
