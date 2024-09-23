package com.example.lubak.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Environment
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.lubak.api.RetrofitClient
import com.example.lubak.model.PredictionCallback
import com.example.lubak.model.PredictionResponse
import com.example.lubak.model.UploadResponse
import com.example.lubak.state.CameraState
import com.example.lubak.state.LocationState
import com.google.android.gms.location.FusedLocationProviderClient
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class CameraViewModel(fusedLocationClient:FusedLocationProviderClient): ViewModel() {

    private val _capturedImagePath = MutableStateFlow<String?>(null)
    val capturedImagePath: StateFlow<String?> = _capturedImagePath.asStateFlow()


    private val _cameraState = MutableStateFlow(CameraState())
    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()
    var sharedByteArray: ByteArray? = null
    var sharedLocation: Location? = null
    private val _locationState = MutableStateFlow(LocationState())
    val locationState: StateFlow<LocationState> = _locationState.asStateFlow()
    val fusedLocationClient = fusedLocationClient

    fun checkLocationPermission(context:Context){
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        _locationState.value = LocationState(hasLocationPermission = hasPermission)
    }

    fun requestLocationPermission(launcher: ManagedActivityResultLauncher<String, Boolean>) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun checkCameraPermission(context: Context) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        _cameraState.value = CameraState(hasCameraPermission = hasPermission)
    }

    fun requestCameraPermission(launcher: ManagedActivityResultLauncher<String, Boolean>) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    fun captureImage(
        imageCapture: ImageCapture,
        context: Context,
        scope: CoroutineScope,
        onCaptureComplete: () -> Unit
    ) {
        // Create a file to save the image
        val photoFile = createFile(context)

        // Set up the metadata
        val metadata = ImageCapture.Metadata().apply {
            // Set rotation based on device orientation
            isReversedHorizontal = false // Update if needed
        }

        // Capture the image
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .setMetadata(metadata)
            .build()

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                @SuppressLint("MissingPermission")
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d("CameraViewModel", "Image saved successfully: ${photoFile.absolutePath}")
                    _capturedImagePath.value = photoFile.absolutePath
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            sharedLocation = location
                        }

                    onCaptureComplete() // Notify that capture is complete
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraViewModel", "Error capturing image: ${exception.message}", exception)
                }
            })
    }

    private fun createFile(context: Context): File {
        // Create a directory and a unique file name for the image
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(directory, "${System.currentTimeMillis()}.jpg")
    }



    suspend fun getCameraProvider(context: Context): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(context).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(context))
        }
    }

     suspend fun uploadImage(context:Context, filePath: String, callback: (String) -> Unit) {
        val file = File(filePath)
        // Compress the image
        val compressedFile = Compressor.compress(context, File(filePath)) {
            quality(75) // Set quality (1-100)
            resolution(800, 800)
            size(2_097_152) // 2 MB
        }
        val requestFile = MultipartBody.Part.createFormData("file", compressedFile.name, compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull()))
        RetrofitClient.instance.uploadFile(requestFile).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                if (response.isSuccessful) {
                    Log.d("Upload", "File uploaded successfully: ${response.body()}")
                    callback(response.body()!!.fileName)
                } else {
                    Log.e("Upload", "Upload failed: ${response.message()}")

                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.e("Upload", "Upload error: ${t.message}")
            }
        })

    }


    fun predictPotholeImage(fileName: String, callback: PredictionCallback) {
        Log.d("Predict","Predict Start")
        RetrofitClient.instance.predict(fileName).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
                        // Assuming it contains a list of predictions
                        val confidenceLevel = it.predictions.firstOrNull()?.confidence

                        if (confidenceLevel != null) {
                            callback.onSuccess(confidenceLevel)
                        } else {
                            callback.onError("No predictions found.")
                        }
                    } ?: callback.onError("Response body is null.")
                } else {
                    callback.onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                callback.onError("Failure: ${t.message}")
            }
        })
    }












}