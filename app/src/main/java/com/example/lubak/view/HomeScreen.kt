package com.example.lubak.view

import DataStoreManager
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.lubak.R
import com.example.lubak.model.PotholeModel
import com.example.lubak.ui.theme.LubakTheme
import com.example.lubak.viewmodel.HomeViewModel
import com.example.lubak.viewmodel.LoginViewModel

import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener



@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController,homeViewModel: HomeViewModel = viewModel()){
    val context = LocalContext.current


        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
        homeViewModel.fetchPotholes()
        }
        var potholes = homeViewModel.potholes
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text("Lubak", modifier = Modifier.padding(16.dp))
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text(text = "Profile") },
                        selected = false,
                        onClick = {}
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Logout") },
                        selected = false,
                        onClick = {
                            scope.launch {
                                DataStoreManager.clearToken(context)
                            }

                            navController.popBackStack()
                            navController.navigate(Screen.LoginOrRegisterScreen.route)
                        }
                    )
                }
            },
            gesturesEnabled = !drawerState.isClosed,

            ) {

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    MyTopAppBar(scope, drawerState)
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        navController.navigate(Screen.CameraScreen.route)
                    }) {
                        Icon(Icons.Filled.Add,"Contribute Pothole")
                    }
                }
            ) { innerPadding ->

                MapScreen(modifier = Modifier.padding(innerPadding),potholes = potholes )

            }
        }

}

@OptIn(MapboxExperimental::class)
@Composable
fun LubakMap(modifier: Modifier = Modifier) {

    MapboxMap(
        modifier = modifier,
        mapViewportState = MapViewportState().apply {
            setCameraOptions {
                zoom(1.0)
                center(Point.fromLngLat(121.0244, 14.5547))
                pitch(0.0)
                bearing(0.0)
            }
        },
    ){

    }

}
@Composable
fun MapBoxMap(
    modifier: Modifier = Modifier,
    points: List<Point>,
    onMarkerClick: (Point) -> Unit // Callback for when a marker is clicked
) {
    val context = LocalContext.current
    val marker = remember(context) {
        context.getDrawable(R.drawable.marker)!!.toBitmap()
    }
    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }
    AndroidView(
        factory = {
            MapView(it).also { mapView ->
                mapView.getMapboxMap().loadStyleUri(Style.TRAFFIC_DAY)
                val annotationApi = mapView.annotations
                pointAnnotationManager = annotationApi.createPointAnnotationManager()
            }
        },
        update = { mapView ->
            pointAnnotationManager?.let {
                it.deleteAll() // Clear existing markers
                points.forEach { point ->
                    val pointAnnotationOptions = PointAnnotationOptions()
                        .withPoint(point)
                        .withIconImage(marker)
                    val pointAnnotation = it.create(pointAnnotationOptions)

                    // Set up click listener for the marker
                    it.addClickListener() { clickedPointAnnotation ->
                        onMarkerClick(clickedPointAnnotation.point)
                        true
                    }
                }
                if (points.isNotEmpty()) {
                    mapView.getMapboxMap()
                        .flyTo(CameraOptions.Builder().zoom(16.0).center(points.first()).build())
                }
            }
        },
        modifier = modifier
    )
}



@Composable
fun MapScreen(modifier: Modifier,potholes: List<PotholeModel>? = null) {
    val context = LocalContext.current
    val makatiMarkers = potholes?.map {
        Point.fromLngLat(it.longitude.toDouble(), it.latitude.toDouble()) // Adjust according to your PotholeModel structure
    } ?: listOf()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        MapBoxMap(
            points = makatiMarkers,
            onMarkerClick = { clickedPoint ->
                // Handle marker click here
                // For example, show a Toast or navigate to another screen
                Toast.makeText(context, "Clicked marker at: ${clickedPoint.latitude()}, ${clickedPoint.longitude()}", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(scope: CoroutineScope, drawerState: DrawerState) {

    TopAppBar(colors = topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    ), title = {
        Text("Lubak")
    }, actions = {
        IconButton(onClick = {
            scope.launch {
                drawerState.apply {
                    if (isClosed) open() else close()
                }
            }
        }) {
            Icon(Icons.Filled.MoreVert, contentDescription = "More options")
        }
    })
}


