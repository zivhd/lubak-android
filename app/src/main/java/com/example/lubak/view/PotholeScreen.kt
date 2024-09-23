package com.example.lubak.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun PotholeScreen(potholeId: String?, navController: NavController) {
    Text(text = "Pothole ID: $potholeId")
}
