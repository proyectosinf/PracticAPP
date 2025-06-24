package com.mobivery.fct25.app.common.designsystem.component.buttons

import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.theme.AppColors
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.HEIGHT_CIRCULAR_BUTTON
import com.mobivery.fct25.app.common.designsystem.theme.HEIGHT_ICON_BUTTON
import com.mobivery.fct25.app.common.designsystem.theme.RADIUS_FOR_BIG_BUTTON
import com.mobivery.fct25.app.common.designsystem.theme.RADIUS_FOR_MEDIUM_BUTTON
import com.mobivery.fct25.app.common.designsystem.theme.RADIUS_FOR_SMALL_BUTTON
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_M
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XS
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_XXS
import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class ButtonSize(
    val verticalPadding: Dp,
    val horizontalPadding: Dp,
    val radius: Dp,
) {
    SMALL(verticalPadding = SPACING_XXS, horizontalPadding = SPACING_XS, radius = RADIUS_FOR_SMALL_BUTTON),
    MEDIUM(verticalPadding = SPACING_XS, horizontalPadding = SPACING_XS, radius = RADIUS_FOR_MEDIUM_BUTTON),
    BIG(verticalPadding = SPACING_S, horizontalPadding = SPACING_XS, radius = RADIUS_FOR_BIG_BUTTON)
}

@Composable
private fun BaseButton(
    text: String?,
    size: ButtonSize,
    enabled: Boolean,
    @DrawableRes iconResource: Int?,
    containerColor: Color,
    containerPressedColor: Color,
    contentColor: Color,
    contentPressedColor: Color,
    disabledContainerColor: Color,
    disabledContentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentContentColor = when {
        !enabled -> disabledContentColor
        isPressed -> contentPressedColor
        else -> contentColor
    }

    val currentContainerColor = if (isPressed) {
        containerPressedColor
    } else {
        containerColor
    }

    val isIconOnly = text.isNullOrEmpty()

    val buttonShape = if (isIconOnly) {
        CircleShape
    } else {
        RoundedCornerShape(size.radius)
    }

    val padding = if (isIconOnly) {
        PaddingValues(all = size.verticalPadding)
    } else {
        PaddingValues(horizontal = size.horizontalPadding, vertical = size.verticalPadding)
    }

    val finalModifier = if (isIconOnly) {
        modifier.size(HEIGHT_CIRCULAR_BUTTON)
    } else {
        modifier
    }

    Button(
        modifier = finalModifier,
        onClick = onClick,
        shape = buttonShape,
        interactionSource = interactionSource,
        contentPadding = padding,
        colors = ButtonDefaults.buttonColors(
            containerColor = currentContainerColor,
            contentColor = currentContentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor,
        ),
        enabled = enabled,
    ) {
        iconResource?.let {
            Icon(
                painterResource(id = iconResource),
                contentDescription = null,
                tint = currentContentColor,
                modifier = Modifier.size(HEIGHT_ICON_BUTTON)
            )
            Spacer(modifier = Modifier.width(SPACING_XXS))
        }
        text?.let {
            Text(
                text.toUpperCase(Locale.current),
                color = currentContentColor,
                style = AppTypographies.bodyLarge
            )
        }
    }
}

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    size: ButtonSize = ButtonSize.MEDIUM,
    enabled: Boolean = true,
    @DrawableRes iconResource: Int? = null,
    onClick: () -> Unit,
) {
    BaseButton(
        text = text,
        size = size,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        iconResource = iconResource,
        containerColor = AppColors.primary,
        containerPressedColor = AppColors.secondary,
        contentColor = AppColors.onPrimary,
        contentPressedColor = AppColors.onPrimary,
        disabledContainerColor = AppColors.backgroundVariant,
        disabledContentColor = AppColors.onBackgroundVariant,
    )
}

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    size: ButtonSize = ButtonSize.MEDIUM,
    enabled: Boolean = true,
    @DrawableRes iconResource: Int? = null,
    onClick: () -> Unit,
) {
    BaseButton(
        text = text,
        size = size,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        iconResource = iconResource,
        containerColor = AppColors.primaryContainer,
        containerPressedColor = AppColors.secondary,
        contentColor = AppColors.onPrimaryContainer,
        contentPressedColor = AppColors.onPrimaryContainer,
        disabledContainerColor = AppColors.backgroundVariant,
        disabledContentColor = AppColors.onBackgroundVariant,
    )
}

@Composable
fun TertiaryButton(
    text: String? = null,
    size: ButtonSize = ButtonSize.MEDIUM,
    enabled: Boolean = true,
    @DrawableRes iconResource: Int? = null,
    modifier: Modifier = Modifier,
    textColor: Color = AppColors.primary,
    onClick: () -> Unit,
) {
    BaseButton(
        text = text,
        size = size,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        iconResource = iconResource,
        containerColor = Color.Transparent,
        containerPressedColor = Color.Transparent,
        contentColor = textColor,
        contentPressedColor = textColor,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = AppColors.onSurfaceSecondary,
    )
}

@Composable
fun LinkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = { onClick() },
        modifier = modifier
    ) {
        Text(
            text,
            style = AppTypographies.bodyMedium.copy(
                textDecoration = TextDecoration.Underline
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(SPACING_M),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                PrimaryButton(
                    text = "Small",
                    size = ButtonSize.SMALL,
                    iconResource = R.drawable.ic_arrow_back,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(SPACING_M))
                PrimaryButton(
                    text = "Medium",
                    size = ButtonSize.MEDIUM,
                    iconResource = R.drawable.ic_arrow_back,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(SPACING_M))
                Column {
                    PrimaryButton(
                        text = "Big",
                        size = ButtonSize.BIG,
                        iconResource = R.drawable.ic_arrow_back,
                        onClick = {}
                    )
                    Spacer(modifier = Modifier.height(SPACING_M))
                    PrimaryButton(
                        text = "Big disabled",
                        size = ButtonSize.BIG,
                        iconResource = R.drawable.ic_arrow_back,
                        enabled = false,
                        onClick = {}
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                PrimaryButton(
                    iconResource = R.drawable.ic_arrow_back,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(SPACING_M))
                Text("Icon")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                PrimaryButton(
                    iconResource = R.drawable.ic_arrow_back,
                    enabled = false,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(SPACING_M))
                Text("Icon disabled")
            }
            PrimaryButton(
                text = "Just text",
                size = ButtonSize.MEDIUM,
                onClick = {},
                modifier = Modifier.width(200.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SecondaryButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(SPACING_M),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                SecondaryButton (
                    text = "Small",
                    size = ButtonSize.SMALL,
                    iconResource = R.drawable.ic_arrow_back,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(SPACING_M))
                SecondaryButton(
                    text = "Medium",
                    size = ButtonSize.MEDIUM,
                    iconResource = R.drawable.ic_arrow_back,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(SPACING_M))
                Column {
                    SecondaryButton(
                        text = "Big",
                        size = ButtonSize.BIG,
                        iconResource = R.drawable.ic_arrow_back,
                        onClick = {}
                    )
                    Spacer(modifier = Modifier.height(SPACING_M))
                    SecondaryButton(
                        text = "Big disabled",
                        size = ButtonSize.BIG,
                        iconResource = R.drawable.ic_arrow_back,
                        enabled = false,
                        onClick = {}
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                SecondaryButton(
                    iconResource = R.drawable.ic_arrow_back,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(SPACING_M))
                Text("Icon")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                SecondaryButton(
                    iconResource = R.drawable.ic_arrow_back,
                    enabled = false,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(SPACING_M))
                Text("Icon disabled")
            }
            SecondaryButton(
                text = "Just text",
                size = ButtonSize.MEDIUM,
                onClick = {},
                modifier = Modifier.width(200.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TertiaryButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(SPACING_M),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                TertiaryButton(
                    text = "Small",
                    size = ButtonSize.SMALL,
                    iconResource = R.drawable.ic_arrow_back,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(SPACING_M))
                TertiaryButton(
                    text = "Medium",
                    size = ButtonSize.MEDIUM,
                    iconResource = R.drawable.ic_arrow_back,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(SPACING_M))
                Column {
                    TertiaryButton(
                        text = "Big",
                        size = ButtonSize.BIG,
                        iconResource = R.drawable.ic_arrow_back,
                        onClick = {}
                    )
                    Spacer(modifier = Modifier.height(SPACING_M))
                    TertiaryButton(
                        text = "Big disabled",
                        size = ButtonSize.BIG,
                        iconResource = R.drawable.ic_arrow_back,
                        enabled = false,
                        onClick = {}
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TertiaryButton(
                    iconResource = R.drawable.ic_arrow_back,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(SPACING_M))
                Text("Icon")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                TertiaryButton(
                    iconResource = R.drawable.ic_arrow_back,
                    enabled = false,
                    onClick = {}
                )
                Spacer(modifier = Modifier.width(SPACING_M))
                Text("Icon disabled")
            }
            TertiaryButton(
                text = "Just text",
                size = ButtonSize.MEDIUM,
                onClick = {},
                modifier = Modifier.width(200.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LinkButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(SPACING_M),
            verticalArrangement = Arrangement.spacedBy(SPACING_S)
        ) {
            LinkButton(
                text = "_Link Button",
                onClick = {}
            )
        }
    }
}
