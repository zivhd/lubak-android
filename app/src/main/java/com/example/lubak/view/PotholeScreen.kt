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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lubak.composables.ArsenalButton
import com.example.lubak.model.PotholeCallback
import com.example.lubak.viewmodel.PotholeViewModel
import kotlinx.coroutines.launch

@Composable
fun PotholeScreen(potholeId: Int?, navController: NavController) {
    val potholeViewModel : PotholeViewModel = viewModel()
    LaunchedEffect(Unit) {
        if (potholeId != null) {
            potholeViewModel.fetchPothole(potholeId)
        }
    }
    val pothole = potholeViewModel.pothole





    if (pothole != null) {
        Column(modifier = Modifier.fillMaxSize()) {

            Text(text = "Pothole ID: ${pothole.id}")
            Text(text = "Pothole latitude: ${pothole.latitude}")
            Text(text = "Pothole longitude: ${pothole.longitude}")
            Text(text = "Pothole confidence: ${pothole.confidence_level}")

        }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        AsyncImage(model = "https://lubakstorage.blob.core.windows.net/photos/${pothole.file_name}", contentDescription = "Pothole Image",modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            contentScale = ContentScale.Crop )


        Column(
            modifier = Modifier
                .padding(8.dp)
                .padding(bottom = 32.dp),
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
                        Text(
                            "%.4f".format(pothole.confidence_level),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )


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
                            text = "%.4f".format(pothole.latitude),
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
                            text = "%.4f".format(pothole.longitude),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
        }




        }
    }

}




