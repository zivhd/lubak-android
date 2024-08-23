package com.example.lubak.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.LubakTheme
import com.example.lubak.R
import com.example.lubak.composables.ArsenalButton

@Composable
fun LoginScreen() {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var email by rememberSaveable { mutableStateOf("") }

    LubakTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Welcome back!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Image(
                    painterResource(R.drawable.pothole),
                    "Login Image",
                    modifier = Modifier.size(50.dp)
                )
            }



            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Login to your account")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                placeholder = { Text("Email Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Default.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                }
                               },
                        modifier = Modifier
                        .fillMaxWidth()
                    .padding(horizontal = 20.dp)



                )
            Text(
                "Forgot Password?",
                fontWeight = FontWeight.Thin,
                fontSize = 12.sp,
                modifier = Modifier
                    .align(alignment = Alignment.Start)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))
            ArsenalButton(
                onClick = {},
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = "Login"
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row {
                Text(
                    "Not yet registered? ",
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp,
                )
                Text(
                    "Click Here!",
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp,
                    color = Color.Blue
                )
            }

        }
    }


}