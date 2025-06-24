package com.mobivery.fct25.app.common.designsystem.component.topbar

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypography
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_L
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithActions(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleStyle: TextStyle = AppTypography.titleLarge,
    titleColor: Color = AppColors.onBackground,
    containerColor: Color = AppColors.surface,
    navigationIcon: ImageVector = Icons.Rounded.Close,
    navigationContentDescription: String = stringResource(R.string.common_close_text),
    topBarActions: @Composable RowScope.() -> Unit = {},
    onNavigationClick: () -> Unit,
    centerTitle: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
            .statusBarsPadding()
    ) {
        TopAppBar(
            title = {
                if (title != null && !centerTitle) {
                    Text(
                        text = title,
                        style = titleStyle,
                        color = titleColor
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = navigationContentDescription
                    )
                }
            },
            actions = topBarActions,
            modifier = Modifier.align(Alignment.TopCenter),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        if (title != null && centerTitle) {
            Text(
                text = title,
                style = titleStyle,
                color = titleColor,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = SPACING_L),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TopBarWithTitle(
    title: Int,
) {
    Column(
        modifier = Modifier
            .background(AppColors.surface)
            .statusBarsPadding()
            .fillMaxWidth()
    ) {
        TopBarTitle(title = title)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithClosedButton(
    title: Int? = null,
    onCloseClick: () -> Unit,
) {
    TopAppBar(
        title = { TopBarTitle(title = title) },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(R.string.common_close_text)
                )
            }
        },
        modifier = Modifier.statusBarsPadding(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppColors.surface
        )
    )
}

@Composable
private fun TopBarTitle(title: Int? = null) {
    Text(
        text = title?.let { stringResource(it) } ?: "",
        style = AppTypography.titleLarge,
        color = AppColors.onBackground,
        modifier = Modifier.padding(start = SPACING_L, top = SPACING_M)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithActionRight(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleStyle: TextStyle = AppTypography.titleLarge,
    titleColor: Color = AppColors.onBackground,
    containerColor: Color = AppColors.surface,
    closeIcon: ImageVector = Icons.AutoMirrored.Rounded.ExitToApp,
    closeContentDescription: String = stringResource(R.string.common_close_text),
    onRightActionClick: () -> Unit,
    centerTitle: Boolean = false,
) {
    TopAppBar(
        title = {
            title?.let {
                Text(
                    text = it,
                    style = titleStyle,
                    color = titleColor,
                    modifier = if (centerTitle) Modifier.fillMaxWidth() else Modifier,
                    textAlign = if (centerTitle) TextAlign.Center else TextAlign.Start
                )
            }
        },
        navigationIcon = {},
        actions = {
            IconButton(onClick = onRightActionClick) {
                Icon(
                    imageVector = closeIcon,
                    contentDescription = closeContentDescription,
                    tint = AppColors.error
                )
            }
        },
        modifier = modifier.statusBarsPadding(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor
        )
    )
}

@Composable
fun TopBarWithLeftCancel(
    modifier: Modifier = Modifier,
    containerColor: Color = AppColors.surface,
    onCancelClick: () -> Unit,
    title: String? = null,
    titleTextStyle: TextStyle = AppTypography.titleLarge
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(containerColor)
            .padding(horizontal = SPACING_M, vertical = SPACING_S)
    ) {
        title?.let {
            Text(
                text = it,
                style = titleTextStyle,
                color = AppColors.onSurface,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }

        TextButton(
            onClick = onCancelClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Text(
                text = stringResource(R.string.common_cancel_text),
                color = AppColors.error,
                style = AppTypography.bodyLarge
            )
        }
    }
}

@Preview(name = "TopBarWithActions Light", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "TopBarWithActions Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TopBarWithActionsPreview() {
    AppTheme {
        TopBarWithActions(
            title = "Preview Title",
            onNavigationClick = {},
            topBarActions = {
                TextButton(onClick = {}) {
                    Text(text = stringResource(R.string.common_accept_text))
                }
            }
        )
    }
}

@Preview(name = "TopBarWithTitle Light", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "TopBarWithTitle Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TopBarWithTitlePreview() {
    AppTheme {
        Column {
            TopBarWithTitle(
                title = R.string.home_title_text
            )
        }
    }
}

@Preview(name = "TopBarWithClosedButton Light", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "TopBarWithClosedButton Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TopBarWithClosedButtonPreview() {
    AppTheme {
        TopBarWithClosedButton(
            title = R.string.home_title_text,
            onCloseClick = {}
        )
    }
}

@Preview(name = "TopBarWithActionRight Light", showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "TopBarWithActionRight Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TopBarWithActionRightPreview() {
    AppTheme {
        TopBarWithActionRight(
            onRightActionClick = {}
        )
    }
}
