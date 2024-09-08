package com.example.lubak.view

import android.util.Log
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.lubak.R
import com.example.lubak.composables.ArsenalButton
import com.example.lubak.ui.theme.LubakTheme
import com.example.lubak.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    val email = loginViewModel.email.value
    val context = LocalContext.current
    val password = loginViewModel.password.value
    val isPasswordVisible = loginViewModel.isPasswordVisible.value
    val loginResult = loginViewModel.loginResult.value

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
                maxLines = 1,
                value = email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                label = { Text("Email Address") },
                placeholder = { Text("Email Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                maxLines = 1,
                value = password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text("Password") },
                placeholder = { Text("Password") },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    val description = if (isPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { loginViewModel.togglePasswordVisibility() }) {
                        Icon(imageVector = image, contentDescription = description)
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
                onClick = {
                    loginViewModel.login(email, password, context) { success ->
                        Log.d("Login", success.toString())
                        if (success) {
                            navController.popBackStack()
                            navController.navigate(Screen.HomeScreen.route)
                        }


                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = "Login"
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(loginResult, color = Color.Red)

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
