package com.example.lubak

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lubak.view.CameraScreen
import com.example.lubak.view.HomeScreen
import com.example.lubak.view.Screen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route ){
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navController = navController)
        }
        composable(route = Screen.CameraScreen.route){
            CameraScreen()
        }
    }
}