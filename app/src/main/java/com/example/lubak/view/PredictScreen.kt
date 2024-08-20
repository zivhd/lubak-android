package com.example.lubak.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.lubak.api.RetrofitClient
import com.example.lubak.model.PredictionResponse
import com.example.lubak.model.UploadResponse
import com.example.lubak.viewmodel.CameraViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun PredictScreen(cameraViewModel: CameraViewModel) {
    val byteArray = cameraViewModel.sharedByteArray
    val apiService = RetrofitClient.instance
    var predictionResponse by remember { mutableStateOf<PredictionResponse?>(null) }
    var uploadResponse by remember { mutableStateOf<UploadResponse?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    if (byteArray != null) {
        val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("file", "image.jpg", requestBody)

        LaunchedEffect(byteArray) {
            try {
                // Upload the image
                val uploadResult = apiService.upload(imagePart)
                uploadResponse = uploadResult.body()

                try{
                 val predictionResult = apiService.predict(uploadResponse!!.fileName)
                 predictionResponse = predictionResult.body()
                }
                catch (e: Exception) {
                    errorMessage = e.localizedMessage
                }

            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            }
        }
    } else {
        Text("No image to display")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(uploadResponse != null){
            Text("Uploaded file: ${uploadResponse!!.fileName}")
        }

        predictionResponse?.let { response ->
            if (response.predictions.isNotEmpty()) {
                Text("Pothole detected with ${response.predictions[0].confidence} confidence level")
            } else {
                Text("No potholes detected")
            }
        }

        errorMessage?.let {
            Text("Error: $it")
            Log.e("Error", it)
        } ?: run {
            Text("Sending image for prediction...")
        }
    }
}
