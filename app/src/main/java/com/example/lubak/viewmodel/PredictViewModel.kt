package com.example.lubak.viewmodel

import DataStoreManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.lubak.api.RetrofitClient
import com.example.lubak.model.PotholeCallback
import com.example.lubak.model.PotholeModel
import com.example.lubak.model.PredictionCallback
import com.example.lubak.model.PredictionResponse
import com.example.lubak.model.UploadResponse
import com.example.lubak.model.User
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PredictViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)



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

    fun predictPotholeImage(fileName: String, callback: PredictionCallback) {
        Log.d("Predict", "Predict Start")
        isLoading = true
        Log.d("Predict isLoading", "true")
        RetrofitClient.instance.predict(fileName).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {

                isLoading = false
                Log.d("Predict isLoading", "false")
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    responseBody?.let {
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
                isLoading = false
                Log.d("Predict isLoading", "false")
                callback.onError("Failure: ${t.message}")
            }
        })
    }

    suspend fun uploadImage(context:Context, filePath: String, callback: (String) -> Unit) {
        isLoading = true
        val compressedFile = Compressor.compress(context, File(filePath)) {
            quality(75) // Set quality (1-100)
            resolution(800, 800)
            size(2_097_152) // 2 MB
        }
        val requestFile = MultipartBody.Part.createFormData("file", compressedFile.name, compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull()))
        RetrofitClient.instance.uploadFile(requestFile).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                if (response.isSuccessful) {
                    isLoading = false
                    Log.d("Upload", "File uploaded successfully: ${response.body()}")
                    callback(response.body()!!.fileName)
                } else {

                    Log.e("Upload", "Upload failed: ${response.message()}")

                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                isLoading = false
                Log.e("Upload", "Upload error: ${t.message}")
            }
        })

    }

    suspend fun contributePothole(
        context: Context,
        confidence: Float,
        latitude: Float,
        longitude: Float,
        fileName: String,
        callback: PotholeCallback
    ) {
        isLoading = true
        val userFlow: Flow<User?> = DataStoreManager.getUser(context)
        var userId: Int? = null
        userFlow.collect { user ->
            user?.let {
                userId = it.id
                Log.d("User", userId.toString())
                if (userId != null) {
                    Log.d("Contribute", "userId ${userId}")
                    val pothole = PotholeModel(0, latitude, longitude, userId!!, confidence, fileName)
                    RetrofitClient.instance.contribute(pothole).enqueue(object : Callback<PotholeModel> {
                        override fun onResponse(
                            call: Call<PotholeModel>,
                            response: Response<PotholeModel>
                        ) {
                            if (response.isSuccessful) {
                                isLoading = false
                                Log.d("Contribute", "Contribute success: ${response.body()}")
                                callback.onSuccess("Contribute success: ${response.body()}")

                            } else {
                                Log.e("Contribute", "Contribute failed: ${response.message()}")
                                callback.onError("Contribute failed: ${response.message()}")

                            }
                        }

                        override fun onFailure(call: Call<PotholeModel>, t: Throwable) {
                            isLoading = false
                            Log.e("Contribute", "Contribute error: ${t.message}")
                            callback.onError("Contribute error: ${t.message}")
                        }
                    })


                }
            }
        }



    }


}