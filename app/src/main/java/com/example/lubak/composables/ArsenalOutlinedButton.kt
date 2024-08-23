package com.example.lubak.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp


@Composable
fun ArsenalOutlinedButton(onClick :  () -> Unit, modifier: Modifier, text: String, fontSize: TextUnit = 16.sp){
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10)
    ) {
        Text(text = text, fontSize = fontSize )
    }
}