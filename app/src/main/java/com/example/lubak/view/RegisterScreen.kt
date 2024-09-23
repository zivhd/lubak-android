package com.example.lubak.view

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.example.lubak.viewmodel.RegisterViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterScreen(navController: NavController) {



    val registerViewModel: RegisterViewModel = viewModel()
    val isLoading = registerViewModel.isLoading
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Image(
                painterResource(R.drawable.logo),
                "Login Image",
                modifier = Modifier.size(50.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Welcome", fontSize = 28.sp, fontWeight = FontWeight.Bold, color= MaterialTheme.colorScheme.onPrimaryContainer)

            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Register to continue", color= MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = registerViewModel.email,
                    onValueChange = { registerViewModel.onEmailChange(it) },
                    label = { Text("Email") },
                    placeholder = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    isError = registerViewModel.emailError != null,
                    singleLine = true
                )
                registerViewModel.emailError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }
            }





            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = registerViewModel.username,
                    onValueChange = { registerViewModel.onUsernameChange(it) },
                    label = { Text("Username") },
                    placeholder = { Text("Username") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    isError = registerViewModel.usernameError != null,
                            singleLine = true
                )
                registerViewModel.usernameError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }
            }






            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = registerViewModel.firstName,
                    onValueChange = { registerViewModel.onFirstNameChange(it) },
                    label = { Text("First Name") },
                    placeholder = { Text("First Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    isError = registerViewModel.firstNameError != null,
                    singleLine = true
                )

                registerViewModel.firstNameError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }
            }



            Column(modifier = Modifier.fillMaxWidth()) {
                // Last Name Input Field
                OutlinedTextField(
                    value = registerViewModel.lastName,
                    onValueChange = { registerViewModel.onLastNameChange(it) },
                    label = { Text("Last Name") },
                    placeholder = { Text("Last Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    isError = registerViewModel.lastNameError != null
                )
                registerViewModel.lastNameError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }
            }

                Column(modifier = Modifier.fillMaxWidth()) {
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

                            val description =
                                if (registerViewModel.passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = { registerViewModel.onPasswordVisibilityToggle() }) {
                                Icon(imageVector = image, contentDescription = description)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        isError = registerViewModel.passwordError != null
                    )
                    registerViewModel.passwordLengthError?.let { error ->
                        if (!error) {
                            Text(
                                text = "• Password must be at least 8 characters long.", // Add bullet point
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 20.dp)

                            )
                        }

                    }
                    registerViewModel.passwordDigitError?.let { error ->
                        if (!error) {
                            Text(
                                text = "• Password must contain at least one digit.", // Add bullet point
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 20.dp)

                            )
                        }

                    }
                    registerViewModel.passwordUppercaseError?.let { error ->
                        if (!error) {
                            Text(
                                text = "• Password must contain at least one uppercase letter", // Add bullet point
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 20.dp)

                            )
                        }

                    }
                    registerViewModel.passwordSpecialCharacterError?.let { error ->
                        if (!error) {
                            Text(
                                text = "• Password must contain at least one special character.", // Add bullet point
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 20.dp)

                            )
                        }

                    }
                }








            Column(modifier = Modifier.fillMaxWidth()) {
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

                        val description =
                            if (registerViewModel.passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = { registerViewModel.onPasswordVisibilityToggle() }) {
                            Icon(imageVector = image, contentDescription = description)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    isError = registerViewModel.confirmPasswordError != null
                )
                registerViewModel.confirmPasswordError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 20.dp)
                    )
                }

            }




            Spacer(modifier = Modifier.height(16.dp))
            ArsenalButton(
                onClick = {

                    if (registerViewModel.validateFields()) {
                        registerViewModel.register { result ->
                            if (result.success) {
                                Toast.makeText(
                                    context,
                                    result.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("login_screen") {
                                    navController.popBackStack()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    result.message,
                                    Toast.LENGTH_SHORT
                                ).show()


                            }
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Please fix the errors above",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                text = "Register"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    "Already have an account? ",
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Click Here!",
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .clickable(
                            onClick = { navController.navigate("login_screen"){
                                navController.popBackStack()
                            } },

                        )
                )
            }

        }
    }

}
