package com.example.lubak.view

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.lubak.ui.theme.LubakTheme
import com.example.lubak.viewmodel.CameraViewModel

@Composable
fun CameraScreen(cameraViewModel: CameraViewModel,navController: NavController) {
    LubakTheme {
        val context = LocalContext.current
        val cameraState by cameraViewModel.cameraState.collectAsState()
        val locationState by cameraViewModel.locationState.collectAsState()

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            cameraViewModel.checkCameraPermission(context)
            cameraViewModel.checkLocationPermission(context)
        }
        LaunchedEffect(key1 = Unit) {
            cameraViewModel.checkCameraPermission(context)
            cameraViewModel.checkLocationPermission(context)
        }
        when {
            !cameraState.hasCameraPermission -> {
                CameraPermissionNeeded(launcher, cameraViewModel)
            }
            !locationState.hasLocationPermission -> {
                LocationPermissionNeeded(launcher, cameraViewModel)
            }
            cameraState.hasCameraPermission && locationState.hasLocationPermission -> {
                CameraPreviewScreen(cameraViewModel, navController)
            }
        }

    }


}

@Composable
fun CameraPermissionNeeded(launcher:ManagedActivityResultLauncher<String, Boolean>, cameraViewModel:CameraViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text("Camera Permission Needed", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("The application needs access to your camera, which is required to contribute and share the potholes you see!")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { cameraViewModel.requestCameraPermission(launcher) }) {
            Text(text = "Request Camera Permission")
        }
    }

}



@Composable
fun LocationPermissionNeeded(launcher:ManagedActivityResultLauncher<String, Boolean>, cameraViewModel:CameraViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text("Location Permission Needed", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text("The application needs access to your location, which is required to contribute and share the potholes you see!")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { cameraViewModel.requestLocationPermission(launcher) }) {
            Text(text = "Request Location Permission")
        }
    }

}



@Composable
fun CameraPreviewScreen(cameraViewModel: CameraViewModel,navController: NavController) {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val coroutineScope = rememberCoroutineScope()
    val previewView = remember {
        PreviewView(context)
    }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }
    LaunchedEffect(lensFacing) {
        val cameraProvider = cameraViewModel.getCameraProvider(context)
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
    ) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
        Button(
            onClick = {
                cameraViewModel.captureImage(imageCapture,context,scope = coroutineScope){
                    navController.navigate(Screen.PredictScreen.route)
                }




             },
            modifier = Modifier.padding(bottom = 30.dp)
        ) {
            Text(text = "Capture Image")
        }
    }
}



