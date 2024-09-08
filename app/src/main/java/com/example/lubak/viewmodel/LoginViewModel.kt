package com.example.lubak.viewmodel

import DataStoreManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lubak.api.RetrofitClient
import com.example.lubak.model.LoginModel
import com.example.lubak.model.LoginResponse
import com.example.lubak.model.RegisterResponse
import com.example.lubak.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val apiService = RetrofitClient.instance

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _isPasswordVisible = mutableStateOf(false)
    val isPasswordVisible: State<Boolean> = _isPasswordVisible

    private val _loginResult = mutableStateOf("")
    val loginResult: State<String> = _loginResult

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun togglePasswordVisibility() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun login(username: String, password: String, context: Context, callback: (Boolean) -> Unit) {

        val loginModel = LoginModel(username, password)

            try {
                val call = apiService.loginUser(loginModel)
                call.enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                viewModelScope.launch {
                                    response.body()!!.token?.let { it1 ->
                                        DataStoreManager.saveToken(context,
                                            it1
                                        )
                                    }
                                }
                                Log.d("Login", "$it")
                                callback(true)
                            } ?: run {
                                Log.e("Login", "Response body is null")
                                callback(false)
                            }
                        } else {
                            Log.e("Login", "Error: ${response.code()} ${response.message()}")
                            callback(false)
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.e("Login", "Error during login", t)
                        callback(false)
                    }

                })
            } catch (e: Exception) {
                Log.e("Login", "Exception during Login", e)
            }

    }
}
