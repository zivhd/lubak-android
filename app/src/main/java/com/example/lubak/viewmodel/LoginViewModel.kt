package com.example.lubak.viewmodel

import DataStoreManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lubak.api.RetrofitClient

import com.example.lubak.model.LoginRequest
import com.example.lubak.model.LoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    fun login(username: String, password: String,context:Context, onResult: (Boolean) -> Unit) {
        val request = LoginRequest(username, password)
        RetrofitClient.instance.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    Log.d("Login",response.body()?.token ?: "Success")
                    response.body()?.let {
                        viewModelScope.launch {
                            response.body()!!.token?.let { it1 ->
                                DataStoreManager.saveToken(
                                    context,
                                    it1
                                )
                            }}}

                    onResult(true)
                } else {
                    onResult(false)
                    Log.d("Login","Error: ${response.message()}")

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Login","Failure: ${t.message}")
            }
        })
    }


}
