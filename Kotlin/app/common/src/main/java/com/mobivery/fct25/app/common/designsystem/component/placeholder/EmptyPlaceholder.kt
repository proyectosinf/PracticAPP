package com.mobivery.fct25.app.common.designsystem.component.placeholder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.mobivery.fct25.app.common.R
import com.mobivery.fct25.app.common.designsystem.theme.AppTheme
import com.mobivery.fct25.app.common.designsystem.theme.AppTypographies
import com.mobivery.fct25.app.common.designsystem.theme.EMPTY_PLACEHOLDER_MARGIN_TOP
import com.mobivery.fct25.app.common.designsystem.theme.SPACING_S

@Composable
fun EmptyPlaceholder(text: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = EMPTY_PLACEHOLDER_MARGIN_TOP,
                bottom = SPACING_S,
                start = SPACING_S,
                end = SPACING_S
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_icon),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(SPACING_S))
        Text(
            text,
            style = AppTypographies.bodyLarge
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun EmptyPlaceholderPreview(){
    AppTheme {
        EmptyPlaceholder(text = "_Empty placeholder text")
    }
}