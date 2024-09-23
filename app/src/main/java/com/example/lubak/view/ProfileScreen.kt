package com.example.lubak.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lubak.model.User
import com.example.lubak.viewmodel.ProfileViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.lubak.composables.CustomNavigationBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val viewModel: ProfileViewModel = viewModel()
    var user by remember { mutableStateOf<User?>(null) }
    var loading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    var selectedItemIndex by rememberSaveable(){
        mutableStateOf(1)
    }
    LaunchedEffect(Unit) {
        viewModel.getUserData(context) { retrievedUser ->
            user = retrievedUser
            loading = false
        }
    }

    if (loading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    } else {
        user?.let {
            // Display user data
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(title = { Text(text = "Profile")})
                },
                bottomBar = {
                    CustomNavigationBar(
                        navController = navController,
                        selectedItemIndex = selectedItemIndex,
                        onItemSelected = { index -> selectedItemIndex = index }
                    )
                }
            ){innerPadding->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Username: ${it.userName}")
                    Text(text = "First Name: ${it.firstName}")
                    Text(text = "Last Name: ${it.lastName}")
                    Text(text = "Email: ${it.email}")
                    Text(text = "Created At: ${it.createdAt ?: "N/A"}")
                    Spacer(modifier = Modifier.height(20.dp))

                    // Logout Button
                    Button(onClick = {
                        viewModel.viewModelScope.launch {
                            viewModel.logout(context, navController)
                        }
                    }) {
                        Text("Logout")
                    }
                }
            }


        }
    }
}