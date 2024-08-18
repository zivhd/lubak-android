package com.example.lubak

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lubak.view.CameraScreen
import com.example.lubak.view.HomeScreen
import com.example.lubak.view.PredictScreen
import com.example.lubak.view.Screen
import com.example.lubak.viewmodel.CameraViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val cameraViewModel: CameraViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.HomeScreen.route ){
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navController = navController)
        }
        composable(route = Screen.CameraScreen.route){
            CameraScreen(navController = navController, cameraViewModel = cameraViewModel)
        }
        composable(route = Screen.PredictScreen.route){
            PredictScreen(cameraViewModel=cameraViewModel)
        }
    }
}