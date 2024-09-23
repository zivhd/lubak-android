package com.example.lubak.view

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home_screen")
    object CameraScreen : Screen("camera_screen")
    object PredictScreen : Screen("predict_screen")
    object LoginScreen: Screen("login_screen")
    object LoginOrRegisterScreen: Screen("login_or_register_screen")
    object RegisterScreen: Screen ("register_screen")
    object PotholeScreen : Screen("pothole_screen/{potholeId}") {
        fun createRoute(potholeId: Int) = "pothole_screen/$potholeId"
    }
}
