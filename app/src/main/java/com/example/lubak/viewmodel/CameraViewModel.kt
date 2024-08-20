package com.example.lubak.viewmodel

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lubak.state.CameraState
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CameraViewModel: ViewModel() {


    private val _cameraState = MutableStateFlow(CameraState())
    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()
    var sharedByteArray: ByteArray? = null

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


    fun captureImage(imageCapture: ImageCapture, context: Context, scope: CoroutineScope,onCaptureComplete: () -> Unit) {
        val name = "CameraxImage.jpeg"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val uri = outputFileResults.savedUri ?: return
                    Log.d("Success", "Image saved to: $uri")

                    scope.launch {
                        try {

                            val file = createFileFromUri(context,uri)
                            val compressedImageFile = Compressor.compress(context, file!!) {
                                resolution(1280, 720)
                                quality(80)
                                size(2_097_152) // 2 MB
                            }
                            val byteArray = compressedImageFile.readBytes()
                            // Delete the image file after conversion
                            deleteFile(context.contentResolver, uri)
                            Log.d("Success", "Success  to read image or delete image")
                            sharedByteArray = byteArray
                            onCaptureComplete()
                        } catch (e: IOException) {
                            Log.d("Failed", "Failed to read image or delete image: $e")
                            sharedByteArray = null
                            onCaptureComplete()
                        }
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.d("Failed", "Image capture failed: $exception")
                    sharedByteArray = null
                    onCaptureComplete()
                }
            })
    }

    @Throws(IOException::class)
    suspend fun uriToByteArray(context: Context, uri: Uri): ByteArray {
        return withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            } ?: throw IOException("Unable to open input stream for URI: $uri")
        }
    }

    fun deleteFile(contentResolver: ContentResolver, uri: Uri) {
        try {
            contentResolver.delete(uri, null, null)
            Log.d("Success", "Image file deleted: $uri")
        } catch (e: Exception) {
            Log.d("Failed", "Failed to delete image file: $e")
        }
    }


    suspend fun getCameraProvider(context: Context): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(context).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(context))
        }
    }

    fun createFileFromUri(context: Context, uri: Uri): File? {
        return try {
            // Get the input stream from the URI
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                // Create a temporary file
                val tempFile = File.createTempFile("image_", ".jpg", context.cacheDir)
                // Write the input stream to the file
                FileOutputStream(tempFile).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } > 0) {
                        outputStream.write(buffer, 0, length)
                    }
                }
                tempFile
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }





}