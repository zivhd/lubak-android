package com.example.lubak.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lubak.api.RetrofitClient
import com.example.lubak.model.PredictionResponse
import com.example.lubak.viewmodel.CameraViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun PredictScreen(cameraViewModel: CameraViewModel) {
    val byteArray = cameraViewModel.sharedByteArray
    val context = LocalContext.current
    val apiService = RetrofitClient.instance
    var predictionResponse by remember { mutableStateOf<PredictionResponse?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    if (byteArray != null) {
        // Convert byteArray to MultipartBody.Part
        val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", "image.jpg", requestBody)

        LaunchedEffect(byteArray) {
            try {
                val response = apiService.predict(imagePart)
                predictionResponse = response.body()
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }


    } else {
        Text("No image to display")
    }

    Column(
        modifier = Modifier.fillMaxSize()
        , verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (predictionResponse != null) {
            Text("Pothole Detected")
        } else if (errorMessage != null) {
            Text("Error: $errorMessage")
            Log.e("Error","$errorMessage")
        } else {
            Text("Sending image for prediction...")
        }
    }
}




