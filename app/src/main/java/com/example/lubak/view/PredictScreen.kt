package com.example.lubak.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lubak.composables.ArsenalButton
import com.example.lubak.model.PredictionCallback
import com.example.lubak.viewmodel.CameraViewModel
import com.example.lubak.viewmodel.PredictViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PredictScreen(
    cameraViewModel: CameraViewModel,

) {
    val predictViewModel: PredictViewModel = viewModel()
    val imagePath by cameraViewModel.capturedImagePath.collectAsState()
    val context = LocalContext.current
    val location = cameraViewModel.sharedLocation
    var blobPath by remember { mutableStateOf<String?>(null) }
    var confidenceLevel by remember { mutableStateOf<Float?>(null) }

    var hasPredicted by remember { mutableStateOf(false) }  // Add a flag


    if (imagePath != null && location != null) {

        LaunchedEffect(imagePath) {
            imagePath?.let {
                if (!hasPredicted) {
                    Log.d("PredictScreen", "Starting image upload...")
                    predictViewModel.uploadImage(context, it) { filepath ->
                        Log.d("PredictScreen", "Image uploaded: $filepath")
                        blobPath = filepath
                        hasPredicted = true

                        predictViewModel.predictPotholeImage(filepath, object : PredictionCallback {
                            override fun onSuccess(confidence: Float) {
                                confidenceLevel = confidence
                                Log.d("Predict", "Prediction success: $confidence")

                            }

                            override fun onError(errorMessage: String) {
                                Log.e("Predict", "Prediction error: $errorMessage")
                                confidenceLevel = 0.0f

                            }
                        })
                    }
                }
            }
        }





        if (predictViewModel.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            val bitmap = predictViewModel.decodeSampledBitmapFromFile(imagePath!!, 1200, 1200)

            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Text("Failed to load image.")
            }

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Use spacedBy for vertical spacing
            ) {
                Text(
                    "Pothole details:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .weight(1f) // Make Box take available space
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text(
                                "Confidence",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 12.sp
                            )
                            confidenceLevel?.let { confidenceLevel ->
                                if (confidenceLevel > 0.0001) {
                                    val formattedConfidenceLevel = "%.4f".format(confidenceLevel)
                                    Text(
                                        formattedConfidenceLevel,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                } else {
                                    Text(
                                        "0.0000",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .weight(1f)
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text(
                                "Latitude:",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "%.4f".format(location.latitude),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .weight(1f)
                    ) {
                        Column(Modifier.padding(8.dp)) {
                            Text(
                                "Longitude:",
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "%.4f".format(location.longitude),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArsenalButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(2.dp),
                        text = "Delete",
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )

                    ArsenalButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(2.dp),
                        text = "Contribute"
                    )
                }


            }
        }
    }
}









