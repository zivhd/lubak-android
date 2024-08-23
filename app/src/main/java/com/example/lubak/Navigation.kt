package com.example.lubak

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lubak.view.CameraScreen
import com.example.lubak.view.HomeScreen
import com.example.lubak.view.LoginOrRegisterScreen
import com.example.lubak.view.LoginScreen
import com.example.lubak.view.PredictScreen
import com.example.lubak.view.RegisterScreen
import com.example.lubak.view.Screen
import com.example.lubak.viewmodel.CameraViewModel
import com.google.android.gms.location.FusedLocationProviderClient

@Composable
fun Navigation(fusedLocationClient:FusedLocationProviderClient) {
    val navController = rememberNavController()
    val cameraViewModel = CameraViewModel(fusedLocationClient)

    NavHost(navController = navController, startDestination = Screen.LoginOrRegisterScreen.route ){
        composable(route = Screen.HomeScreen.route){
            HomeScreen(navController = navController)
        }
        composable(route = Screen.CameraScreen.route){
            CameraScreen(navController = navController, cameraViewModel = cameraViewModel)
        }
        composable(route = Screen.PredictScreen.route){
            PredictScreen(cameraViewModel=cameraViewModel)
        }
        composable(route = Screen.LoginScreen.route){
            LoginScreen()
        }
        composable(route = Screen.LoginOrRegisterScreen.route){
            LoginOrRegisterScreen(navController = navController)
        }
        composable(route = Screen.RegisterScreen.route){
            RegisterScreen(navController = navController)
        }
    }
}