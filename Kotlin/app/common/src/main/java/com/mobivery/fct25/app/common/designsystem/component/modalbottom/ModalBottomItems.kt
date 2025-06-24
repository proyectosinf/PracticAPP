package com.mobivery.fct25.app.common.designsystem.component.modalbottom

import android.Manifest
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.component.dialogs.InfoDialog
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.extension.openAppSettings

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ModalBottomItemWithPermission(
    textResource: Int,
    iconResource: Int,
    permission: String,
    rationaleMessage: String,
    permissionRejectedMessage: String,
    actionWhenGranted: () -> Unit,
) {
    val permissionRequested = rememberSaveable { mutableStateOf(false) }
    val permissionState = if (LocalInspectionMode.current) {
        // Just for preview:
        PermissionStatePreview()
    } else {
        // In the app:
        rememberPermissionState(permission) {
            permissionRequested.value = true
        }
    }
    val canShowRejectedDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var permissionRejectedForever = false
    val rejectedMessageToShow = when {
        !permissionState.status.isGranted && permissionState.status.shouldShowRationale -> {
            // Case permission was rejected once, there is one  more try
            rationaleMessage
        }

        !permissionState.status.isGranted && permissionRequested.value -> {
            // Case permission was rejected twice
            // Permission dialog will not be shown again
            // User needs to go to app settings to enable it.
            permissionRejectedForever = true
            permissionRejectedMessage
        }

        else -> null
    }

    if (rejectedMessageToShow != null) {
        if (canShowRejectedDialog.value) {
            if (permissionRejectedForever) {
                InfoDialog(
                    title = stringResource(id = R.string.common_error_text),
                    message = rejectedMessageToShow,
                    primaryButton = stringResource(id = R.string.common_accept_text),
                    secondaryButton = stringResource(id = R.string.common_open_app_settings_text),
                    onPrimaryButtonClick = {
                        canShowRejectedDialog.value = false
                    },
                    onSecondaryButtonClick = {
                        if (permissionRejectedForever) {
                            context.openAppSettings()
                        }
                        canShowRejectedDialog.value = false
                    },
                )
            } else {
                InfoDialog(
                    title = "",
                    message = rejectedMessageToShow,
                    primaryButton = stringResource(id = R.string.common_accept_text),
                    onPrimaryButtonClick = {
                        canShowRejectedDialog.value = false
                    },
                    onSecondaryButtonClick = {},
                )
            }
        }
    }

    ModalBottomItem(
        textResource = textResource,
        iconResource = iconResource,
        onClick = {
            canShowRejectedDialog.value = true
            permissionState.launchPermissionRequest()
            if (permissionState.status.isGranted) {
                actionWhenGranted()
            }
        }
    )
}

@Composable
fun ModalBottomItem(
    @StringRes textResource: Int,
    @DrawableRes iconResource: Int,
    onClick: () -> Unit,
) {
    TextButton(onClick = onClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = iconResource),
                contentDescription = null,
                tint = AppColors.onSurface
            )
            Spacer(modifier = Modifier.width(SPACING_S))
            Text(
                stringResource(id = textResource),
                style = AppTypographies.titleLarge
            )
        }
    }
}

@ExperimentalPermissionsApi
class PermissionStatePreview : PermissionState {
    override val permission: String
        get() = Manifest.permission.CAMERA
    override val status: PermissionStatus
        get() = PermissionStatus.Denied(shouldShowRationale = true)

    override fun launchPermissionRequest() {
        // Do nothing
    }
}

@Preview(showBackground = true)
@Composable
private fun ModalBottomItemPreview() {
    AppTheme {
        ModalBottomItem(
            textResource = R.string.common_app_name_text,
            iconResource = R.drawable.select_from_gallery_icn,
            onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ModalBottomItemWithPermissionPreview() {
    AppTheme {
        ModalBottomItemWithPermission(
            textResource = R.string.common_app_name_text,
            iconResource = R.drawable.select_from_gallery_icn,
            permission = Manifest.permission.CAMERA,
            rationaleMessage = "Request permission test",
            permissionRejectedMessage = "Rejected",
            actionWhenGranted = {},
        )
    }
}
