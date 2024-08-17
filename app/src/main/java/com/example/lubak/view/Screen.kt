package com.example.lubak.view

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object CameraScreen : Screen("camera_screen")
}