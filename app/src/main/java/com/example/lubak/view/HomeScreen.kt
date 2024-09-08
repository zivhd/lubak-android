package com.example.lubak.view

import DataStoreManager
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lubak.ui.theme.LubakTheme

import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController){
    val context = LocalContext.current

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
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

                LubakMap(modifier = Modifier.padding(innerPadding))

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
    )

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


