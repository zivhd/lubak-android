package com.example.lubak.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.lubak.api.RetrofitClient
import com.example.lubak.model.PredictionCallback
import com.example.lubak.model.PredictionResponse
import com.example.lubak.model.UploadResponse
import com.example.lubak.viewmodel.CameraViewModel
import id.zelory.compressor.decodeSampledBitmapFromFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File



@Composable
fun PredictScreen(cameraViewModel: CameraViewModel) {
    val imagePath by cameraViewModel.capturedImagePath.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val location = cameraViewModel.sharedLocation
    var blobPath by remember { mutableStateOf<String?>(null)}
    var confidenceLevel by remember { mutableStateOf<Float?>(null)}
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (imagePath != null) {
            LaunchedEffect(imagePath) {
                imagePath?.let {

                    cameraViewModel.uploadImage(context, it) { filepath ->
                        blobPath = filepath
                        Log.d("Upload", blobPath!!)

                    }
                }
            }



            val bitmap = decodeSampledBitmapFromFile(imagePath!!, 800, 800) // Change dimensions as needed

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Captured Image",

                )
            } ?: run {
                Text("Failed to load image.")
            }
            if (location != null) {
                Text("Latitude: ${location.latitude}")
                Text("Latitude: ${location.longitude}")
            }

            blobPath?.let {
                Text("blobPath: $blobPath")
                cameraViewModel.predictPotholeImage(it, object : PredictionCallback {
                    override fun onSuccess(confidence: Float) {
                        confidenceLevel = confidence
                        Log.d("Predict", "Confidence: $confidenceLevel")
                    }

                    override fun onError(errorMessage: String) {
                        confidenceLevel = 0.0f
                        Log.e("Predict", errorMessage)
                    }
                })
            }
            confidenceLevel?.let { confidenceLevel->
                if(confidenceLevel > 0.0001) {
                    Text("Pothole Detected with Confidence: $confidenceLevel")
                }
                else{
                    Text("No Pothole Detected")
                }
            }


        } else {
            Text("No image captured yet.")
        }



    }
}

fun decodeSampledBitmapFromFile(filePath: String, reqWidth: Int, reqHeight: Int): Bitmap? {
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeFile(filePath, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(filePath, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height: Int, width: Int) = options.outHeight to options.outWidth
    if (height == 0 || width == 0) return 1

    val halfHeight = height / 2
    val halfWidth = width / 2
    var inSampleSize = 1

    while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
        inSampleSize *= 2
    }
    return inSampleSize
}





//@Composable
//fun PredictScreen(cameraViewModel: CameraViewModel) {
//    val byteArray = cameraViewModel.sharedByteArray
//    val location = cameraViewModel.sharedLocation
//    val apiService = RetrofitClient.instance
//    var predictionResponse by remember { mutableStateOf<PredictionResponse?>(null) }
//    var uploadResponse by remember { mutableStateOf<UploadResponse?>(null) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//
//    if (byteArray != null) {
//        val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
//        val imagePart = MultipartBody.Part.createFormData("file", "image.jpg", requestBody)
//
//        LaunchedEffect(byteArray) {
//            try {
//                // Upload the image
//                val uploadResult = apiService.upload(imagePart)
//                uploadResponse = uploadResult.body()
//
//                try{
//                 val predictionResult = apiService.predict(uploadResponse!!.fileName)
//                 predictionResponse = predictionResult.body()
//                }
//                catch (e: Exception) {
//                    errorMessage = e.localizedMessage
//                }
//
//            } catch (e: Exception) {
//                errorMessage = e.localizedMessage
//            }
//        }
//    } else {
//        Text("No image to display")
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        if(uploadResponse != null){
//            Text("Uploaded file: ${uploadResponse!!.fileName}")
//        }
//
//        predictionResponse?.let { response ->
//            if (response.predictions.isNotEmpty()) {
//                Text("Pothole detected with ${response.predictions[0].confidence} confidence level")
//            } else {
//                Text("No potholes detected")
//            }
//        }
//        Text("Location at latitude: ${location!!.latitude} and longitude ${location.longitude} ")
//
//        errorMessage?.let {
//            Text("Error: $it")
//            Log.e("Error", it)
//        } ?: run {
//            Text("Sending image for prediction...")
//        }
//    }
//}
