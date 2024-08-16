package com.example.lubak.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.compose.LubakTheme
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraScreen() {
    LubakTheme{
        var hasCameraPermission by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            hasCameraPermission = isGranted
        }
        LaunchedEffect(key1 = Unit) {
            hasCameraPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (hasCameraPermission) {
            CameraPreviewScreen()
        } else {
            CameraPermissionNeeded(launcher)
        }
    }


}

@Composable
fun CameraPermissionNeeded(launcher: ManagedActivityResultLauncher<String, Boolean>) {
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
        Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
            Text(text = "Request Camera Permission")
        }
    }

}

@Composable
fun CameraComponent(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
    Text("Camera Permission Given", fontWeight = FontWeight.Bold)}
}

@Composable
fun CameraPreviewScreen() {
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview)
        preview.surfaceProvider = previewView.surfaceProvider
    }
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Capture Image")
        }
    }
}



private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }