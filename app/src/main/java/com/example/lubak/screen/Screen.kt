package com.example.lubak.screen

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object CameraScreen : Screen("camera_screen")
}