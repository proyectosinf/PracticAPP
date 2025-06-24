package com.mobivery.fct25.app.common.designsystem.component.permissions

import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.component.buttons.PrimaryButton
import com.mobivery.fct25.app.common.designsystem.component.dialogs.InfoDialog
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XL
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XS
import com.mobivery.fct25.app.common.extension.openAppSettings
import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

/**
 *  - Reference -
 *
 *  Android permission request workflow:
 *      https://developer.android.com/training/permissions/requesting#workflow_for_requesting_permissions
 *
 *  And the Jetpack Compose permissions library:
 *      https://google.github.io/accompanist/permissions/
 */

/**
 *  Primary button with logic to manage permission request
 *
 *  @permission: sample: "Manifest.permission.CAMERA"
 *  @rationaleMessage: message when rejected for first time
 *  @permissionRejectedMessage: message when rejected forever
 *  @rejectedDialog: to show a dialog when a permission is rejected instead of a Text over the button
 *  @actionWhenGranted: use this instead of your button's onClick!
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PrimaryButtonWithPermission(
    permission: String,
    rationaleMessage: String,
    permissionRejectedMessage: String,
    rejectedDialog: Boolean = false,
    buttonText: String,
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (rejectedMessageToShow != null) {
            if (rejectedDialog) {
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
            } else {
                RejectedPermissionText(
                    rejectedPermissionString = rejectedMessageToShow,
                    isRejectedForever = permissionRejectedForever
                )
            }
        }
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = buttonText,
            onClick = {
                canShowRejectedDialog.value = true
                permissionState.launchPermissionRequest()
                if (permissionState.status.isGranted) {
                    actionWhenGranted()
                }
            },
        )
    }
}

@Composable
fun RejectedPermissionText(
    rejectedPermissionString: String,
    isRejectedForever: Boolean,
) {
    val context = LocalContext.current
    val annotatedLinkString = buildAnnotatedString {
        val settings = stringResource(id = R.string.common_open_app_settings_text)
        val message = "$rejectedPermissionString $settings"
        val startIndex = message.indexOf(settings)
        val endIndex = startIndex + settings.length
        append(message)
        addStyle(
            style = SpanStyle(
                color = AppColors.onSurface,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline
            ), start = startIndex, end = endIndex
        )
    }

    if (isRejectedForever) {
        ClickableText(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = AppColors.error,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(color = AppColors.error.copy(alpha = 0.1f))
                .padding(all = SPACING_XS),
            text = annotatedLinkString,
            onClick = {
                context.openAppSettings()
            }
        )
    } else {
        Text(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = AppColors.error,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(color = AppColors.error.copy(alpha = 0.1f))
                .padding(all = SPACING_XS),
            text = rejectedPermissionString,
            fontSize = 14.sp,
        )
    }
    Spacer(modifier = Modifier.height(SPACING_M))
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

@Preview(showSystemUi = true)
@Composable
fun PrimaryButtonWithPermissionPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = SPACING_S),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        PrimaryButtonWithPermission(
            permission = Manifest.permission.CAMERA,
            rationaleMessage = "Necesitamos que aceptes el permiso para abrir la cámara",
            permissionRejectedMessage = "La app no tiene permiso para abrir la camara, ve a los settings de la app y acéptalos",
            buttonText = "Abrir cámara",
            actionWhenGranted = {},
        )
        Spacer(modifier = Modifier.height(SPACING_XL))
        PrimaryButtonWithPermission(
            permission = Manifest.permission.CAMERA,
            rationaleMessage = "Necesitamos que aceptes el permiso para abrir la cámara",
            permissionRejectedMessage = "La app no tiene permiso para abrir la camara, ve a los settings de la app y acéptalos",
            rejectedDialog = true,
            buttonText = "Abrir cámara con diálogo de permisos",
            actionWhenGranted = {},
        )
    }
}
