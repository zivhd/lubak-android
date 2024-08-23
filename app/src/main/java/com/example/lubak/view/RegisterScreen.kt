package com.example.lubak.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.lubak.viewmodel.RegisterViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegisterScreen() {
    val registerViewModel: RegisterViewModel = viewModel()
    val isLoading = registerViewModel.isLoading

    LubakTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(isLoading){
                CircularProgressIndicator()
            }
            else{
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Welcome", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Image(
                    painterResource(R.drawable.pothole),
                    "Login Image",
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Register to continue")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = registerViewModel.email,
                onValueChange = { registerViewModel.onEmailChange(it) },
                label = { Text("Email") },
                placeholder = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )

            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = registerViewModel.username,
                onValueChange = { registerViewModel.onUsernameChange(it) },
                label = { Text("Username") },
                placeholder = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = registerViewModel.firstName,
                onValueChange = { registerViewModel.onFirstNameChange(it) },
                label = { Text("First Name") },
                placeholder = { Text("First Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )

            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = registerViewModel.lastName,
                onValueChange = { registerViewModel.onLastNameChange(it) },
                label = { Text("Last Name") },
                placeholder = { Text("Last Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )

            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = registerViewModel.password,
                onValueChange = { registerViewModel.onPasswordChange(it) },
                label = { Text("Password") },
                placeholder = { Text("Password") },
                visualTransformation = if (registerViewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (registerViewModel.passwordVisible)
                        Icons.Default.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (registerViewModel.passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { registerViewModel.onPasswordVisibilityToggle() }) {
                        Icon(imageVector = image, description)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = registerViewModel.confirmPassword,
                onValueChange = { registerViewModel.onConfirmPasswordChange(it) },
                label = { Text("Confirm Password") },
                placeholder = { Text("Confirm Password") },
                visualTransformation = if (registerViewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (registerViewModel.passwordVisible)
                        Icons.Default.Visibility
                    else Icons.Filled.VisibilityOff

                    val description = if (registerViewModel.passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { registerViewModel.onPasswordVisibilityToggle() }) {
                        Icon(imageVector = image, description)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            ArsenalButton(
                onClick = { registerViewModel.register() },
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = "Register"
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row() {
                Text(
                    "Already have an account? ",
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

        }}
    }
}
