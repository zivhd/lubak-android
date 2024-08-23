package com.example.lubak.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ArsenalButton(onClick :  () -> Unit, modifier: Modifier, text: String, fontSize: TextUnit = 16.sp){
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10)
    ) {
        Text(text = text, fontSize = fontSize )
    }
}