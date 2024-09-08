package com.example.lubak.view

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.lubak.R
import com.example.lubak.composables.ArsenalButton
import com.example.lubak.composables.ArsenalOutlinedButton
import com.example.lubak.ui.theme.LubakTheme

@Composable
fun LoginOrRegisterScreen(navController: NavController) {

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painterResource(R.drawable.pothole),
                    "Login Image",
                    modifier = Modifier.size(250.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Welcome to Lubak!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(bottom = 50.dp)
            ) {
                ArsenalButton(
                    onClick = {navController.navigate(Screen.LoginScreen.route)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .height(60.dp),
                    text = "Login"

                )
                Spacer(modifier = Modifier.height(16.dp))
                ArsenalOutlinedButton(
                    onClick = {navController.navigate(Screen.RegisterScreen.route)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp)
                        .height(60.dp),
                    text = "Register"
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

}


