package com.example.lubak

import DataStoreManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.flow.Flow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(fusedLocationClient:FusedLocationProviderClient) {
    val navController = rememberNavController()
    val cameraViewModel = CameraViewModel(fusedLocationClient)
    val context = LocalContext.current
    val tokenFlow: Flow<String?> = DataStoreManager.getToken(context)
    var startDestination by rememberSaveable { mutableStateOf(Screen.LoginOrRegisterScreen.route) }

    LaunchedEffect(Unit) {
        tokenFlow.collect { token ->
            // Handle the token
            Log.d("Token", "Retrieved token: $token")
            if(!token.isNullOrEmpty()){
                startDestination = Screen.HomeScreen.route
            }else{
                startDestination = Screen.LoginOrRegisterScreen.route
            }
        }
    }


    NavHost(navController = navController, startDestination = startDestination ){
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
            LoginScreen(navController = navController)
        }
        composable(route = Screen.LoginOrRegisterScreen.route){
            LoginOrRegisterScreen(navController = navController)
        }
        composable(route = Screen.RegisterScreen.route){
            RegisterScreen(navController = navController)
        }
    }
}