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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lubak.view.CameraScreen
import com.example.lubak.view.HomeScreen
import com.example.lubak.view.LoginOrRegisterScreen
import com.example.lubak.view.LoginScreen
import com.example.lubak.view.PotholeScreen
import com.example.lubak.view.PredictScreen
import com.example.lubak.view.RegisterScreen
import com.example.lubak.view.Screen
import com.example.lubak.view.SplashScreen
import com.example.lubak.viewmodel.CameraViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(fusedLocationClient: FusedLocationProviderClient) {
    val navController = rememberNavController()
    val cameraViewModel = CameraViewModel(fusedLocationClient)
    val context = LocalContext.current
    val tokenFlow: Flow<String?> = DataStoreManager.getToken(context)

    // State to hold the current route
    var startDestination by rememberSaveable { mutableStateOf(Screen.SplashScreen.route) }

    LaunchedEffect(Unit) {
        // Display the splash screen for 2 seconds
        delay(2000)

        tokenFlow.collect { token ->
            Log.d("Token", "Retrieved token: $token")
            startDestination = if (!token.isNullOrEmpty()) {
                Screen.HomeScreen.route
            } else {
                Screen.LoginOrRegisterScreen.route
            }

            // Navigate to the determined screen
            navController.navigate(startDestination) {
                // Clear back stack to prevent returning to splash screen
                popUpTo(Screen.SplashScreen.route) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(route = Screen.SplashScreen.route) {
            SplashScreen()
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.CameraScreen.route) {
            CameraScreen(navController = navController, cameraViewModel = cameraViewModel)
        }
        composable(route = Screen.PredictScreen.route) {
            PredictScreen(cameraViewModel = cameraViewModel)
        }
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.LoginOrRegisterScreen.route) {
            LoginOrRegisterScreen(navController = navController)
        }
        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.PotholeScreen.route) { backStackEntry ->
            val potholeId = backStackEntry.arguments?.getString("potholeId")
            PotholeScreen(potholeId, navController)
        }
    }
}

