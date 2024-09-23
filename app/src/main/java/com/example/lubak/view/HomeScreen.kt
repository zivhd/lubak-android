package com.example.lubak.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.lubak.R
import com.example.lubak.model.PotholeModel
import com.example.lubak.viewmodel.HomeViewModel
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
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.scalebar.scalebar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

data class BottomNavigationItem(
    val title:String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route:String
)



@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    val items = listOf(
        BottomNavigationItem(
            title = "Explore",
            selectedIcon = Icons.Filled.Map,
            unselectedIcon = Icons.Outlined.Map,
            route = Screen.HomeScreen.route
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            route = ""
        ),
        BottomNavigationItem(
            title = "Contribute",
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add,
            route = Screen.CameraScreen.route
        )
    )
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel()


    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable(){
        mutableStateOf(0)


    }
    LaunchedEffect(Unit) {
        homeViewModel.fetchPotholes()
    }
    val potholes = homeViewModel.potholes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = "Explore")})
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed {index,item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.route)
                        },
                        label = { Text(item.title) },
                        icon = {
                            Icon(
                                imageVector = if(index == selectedItemIndex){
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    )

                }
            }
        }
    ) { innerPadding ->

            MapScreen(modifier = Modifier.padding(innerPadding),potholes = potholes, navController)

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
    ) {

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
        val drawable = context.getDrawable(R.drawable.marker)?.apply {
            setTint(
                ContextCompat.getColor(
                    context,
                    R.color.error_color
                )
            ) // Set your desired color here
        }
        context.getDrawable(R.drawable.marker)!!.toBitmap(width = 150, height = 150)
    }

    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }

    AndroidView(
        factory = {
            MapView(it).also { mapView ->
                mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)

                // Disable compass and scale
                mapView.compass.updateSettings {
                    enabled = false
                }
                mapView.scalebar.updateSettings {
                    enabled = false
                }

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
fun MapScreen(
    modifier: Modifier,
    potholes: List<PotholeModel>? = null,
    navController: NavController
) {
    val makatiMarkers = potholes?.map {
        Point.fromLngLat(it.longitude.toDouble(), it.latitude.toDouble())
    } ?: listOf()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MapBoxMap(
            points = makatiMarkers,
            onMarkerClick = { clickedPoint ->
                val selectedPothole = potholes?.firstOrNull {
                    it.longitude.toDouble() == clickedPoint.longitude() && it.latitude.toDouble() == clickedPoint.latitude()
                }
                selectedPothole?.let {
                    navController.navigate(Screen.PotholeScreen.createRoute(it.id)) // Use the createRoute method
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(scope: CoroutineScope, drawerState: DrawerState) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
                IconButton(onClick = {
                    scope.launch {
                        drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                }) {
                    Icon(Icons.Filled.Menu, contentDescription = "More options")
                }
                Text("Lubak")
            }
        }
    )
}


