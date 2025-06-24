package com.mobivery.fct25.app.common.designsystem.component.text

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies

@Composable
fun TextWithIcon(
    text: String,
    asyncIcon: String?,
    iconSize: Dp,
    iconContentDescription: String?,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    fontWeight: FontWeight? = null,
    iconEndPadding: Dp = 0.dp,
    iconLine: Int = 0,
) {

    TextWithIcon(text = text,
        iconSize = iconSize,
        modifier = modifier,
        style = style,
        fontWeight = fontWeight,
        iconEndPadding = iconEndPadding,
        iconLine = iconLine,
        content = {
            AsyncImage(
                model = asyncIcon,
                error = painterResource(R.drawable.generic_icn),
                placeholder = painterResource(R.drawable.generic_icn),
                contentDescription = iconContentDescription,
                modifier = Modifier.size(iconSize)
            )
        })
}

@Composable
fun TextWithIcon(
    text: String,
    @DrawableRes iconRes: Int,
    iconSize: Dp,
    iconContentDescription: String?,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    fontWeight: FontWeight? = null,
    iconEndPadding: Dp = 0.dp,
    iconLine: Int = 0,
) {

    TextWithIcon(text = text,
        iconSize = iconSize,
        modifier = modifier,
        style = style,
        fontWeight = fontWeight,
        iconEndPadding = iconEndPadding,
        iconLine = iconLine,
        content = {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = iconContentDescription,
                Modifier.size(iconSize)
            )
        })
}

@Composable
fun TextWithIcon(
    text: String,
    imageVector: ImageVector,
    iconSize: Dp,
    iconTint: Color = LocalContentColor.current,
    iconContentDescription: String?,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    fontWeight: FontWeight? = null,
    iconEndPadding: Dp = 0.dp,
    iconLine: Int = 0,
) {

    TextWithIcon(text = text,
        iconSize = iconSize,
        modifier = modifier,
        style = style,
        fontWeight = fontWeight,
        iconEndPadding = iconEndPadding,
        iconLine = iconLine,
        content = {
            Icon(
                imageVector = imageVector,
                contentDescription = iconContentDescription,
                tint = iconTint,
                modifier = Modifier.size(iconSize)
            )
        })
}

@Composable
fun TextWithIcon(
    text: AnnotatedString,
    @DrawableRes iconRes: Int,
    iconSize: Dp,
    iconContentDescription: String?,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    fontWeight: FontWeight? = null,
    iconEndPadding: Dp = 0.dp,
    iconLine: Int = 0,
) {

    TextWithIcon(text = text,
        iconSize = iconSize,
        modifier = modifier,
        style = style,
        fontWeight = fontWeight,
        iconEndPadding = iconEndPadding,
        iconLine = iconLine,
        content = {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = iconContentDescription,
                Modifier.size(iconSize)
            )
        })
}

@Composable
private fun TextWithIcon(
    text: String,
    iconSize: Dp,
    content: @Composable @UiComposable () -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    fontWeight: FontWeight? = null,
    iconEndPadding: Dp = 0.dp,
    iconLine: Int = 0,
) {
    var lineTop by remember { mutableStateOf(0f) }
    var lineBottom by remember { mutableStateOf(0f) }

    Box(modifier = modifier
        .semantics(mergeDescendants = true) {}) {
        Text(
            text = text,
            style = style,
            fontWeight = fontWeight,
            onTextLayout = { layoutResult ->
                val nbLines = layoutResult.lineCount
                if (nbLines > iconLine) {
                    lineTop = layoutResult.getLineTop(iconLine)
                    lineBottom = layoutResult.getLineBottom(iconLine)
                }
            },
            modifier = Modifier
                .padding(start = iconSize + iconEndPadding)
        )
        Layout(
            content = {
                content()
            }
        ) { measurables, constraints ->
            //Measuring the first item with the given constraints and storing the resulting placeable
            val placeable = measurables[0].measure(constraints)
            layout(constraints.minWidth, constraints.minHeight) {
                // Placing at net half the distance
                placeable.place(0, ((lineBottom - lineTop) / 2 - (iconSize.toPx() / 2)).toInt())
            }
        }
    }
}

@Composable
private fun TextWithIcon(
    text: AnnotatedString,
    iconSize: Dp,
    content: @Composable @UiComposable () -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    fontWeight: FontWeight? = null,
    iconEndPadding: Dp = 0.dp,
    iconLine: Int = 0,
) {
    var lineTop by remember { mutableStateOf(0f) }
    var lineBottom by remember { mutableStateOf(0f) }

    Box(modifier = modifier
        .semantics(mergeDescendants = true) {}) {
        Text(
            text = text,
            style = style,
            fontWeight = fontWeight,
            onTextLayout = { layoutResult ->
                val nbLines = layoutResult.lineCount
                if (nbLines > iconLine) {
                    lineTop = layoutResult.getLineTop(iconLine)
                    lineBottom = layoutResult.getLineBottom(iconLine)
                }
            },
            modifier = Modifier
                .padding(start = iconSize + iconEndPadding)
        )
        Layout(
            content = {
                content()
            }
        ) { measurables, constraints ->
            //Measuring the first item with the given constraints and storing the resulting placeable
            val placeable = measurables[0].measure(constraints)
            layout(constraints.minWidth, constraints.minHeight) {
                // Placing at net half the distance
                placeable.place(0, ((lineBottom - lineTop) / 2 - (iconSize.toPx() / 2)).toInt())
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun TextWithAsyncIconPreview() {
    AppTheme {
        TextWithIcon(
            text = "_Text with icon",
            asyncIcon = "iconUrl",
            iconSize = 24.dp,
            iconContentDescription = null
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, widthDp = 150)
@Composable
fun TextWithResIconPreview() {
    AppTheme {
        TextWithIcon(
            text = "_Text with icon in multiple lines",
            iconRes = R.drawable.generic_icn,
            iconSize = 20.dp,
            iconContentDescription = null
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun TextAnnotatedStringWithResIconPreview() {
    AppTheme {
        TextWithIcon(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontStyle = AppTypographies.bodyLarge.fontStyle)) {
                    append("_First line \n")
                }

                withStyle(style = SpanStyle(fontStyle = AppTypographies.bodyLarge.fontStyle)) {
                    append("_second line")
                }

            },
            iconRes = R.drawable.generic_icn,
            iconSize = 20.dp,
            iconContentDescription = null
        )
    }
}

