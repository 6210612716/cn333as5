package com.example.phonebook.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactColorTag(
    modifier: Modifier = Modifier,
    name: String,
    colorTag: Color,
    size: Dp,
    border: Dp
) {
    var letterProfile = name

    if (name.isNotEmpty()) {
        letterProfile = name.split(" ").joinToString("") { it.first().toString() }
    } else {
        letterProfile = ""
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colors.onSurface.copy(alpha = .02f))
            .border(
                BorderStroke(
                    border,
                    SolidColor(colorTag)
                ),
                CircleShape
            )
    ) {
        Text(
            text = letterProfile,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 9.dp)
        )
    }
}

@Composable
fun ContactOnlyColorTag(
    modifier: Modifier = Modifier,
    colorTag: Color,
    size: Dp,
    border: Dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(colorTag)
            .border(
                BorderStroke(
                    border,
                    SolidColor(colorTag)
                ),
                CircleShape
            )
    )
}

@Composable
fun BigContactColorTag(
    modifier: Modifier = Modifier,
    name: String,
    colorTag: Color,
    size: Dp,
    border: Dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(colorTag)
            .border(
                BorderStroke(
                    border,
                    SolidColor(colorTag)
                ),
                CircleShape
            )
    ) {
        Text(
            text = name,
            fontSize = 70.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 9.dp)
        )
    }
}

@Preview
@Composable
fun ContactColorTagPreview() {
    ContactColorTag(
        colorTag = Color.Red,
        name = "Test",
        size = 40.dp,
        border = 2.dp,
    )
}

@Preview
@Composable
fun ContactOnlyColorTagPreview() {
    ContactOnlyColorTag(
        colorTag = Color.Red,
        size = 40.dp,
        border = 2.dp,
    )
}