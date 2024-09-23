package com.example.lubak.viewmodel


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.lubak.model.User
import com.example.lubak.view.Screen
import kotlinx.coroutines.flow.Flow
import retrofit2.Callback

class ProfileViewModel: ViewModel(){


    suspend fun getUserData(context: Context, callback: (User) -> Unit){
        val userFlow: Flow<User?> = DataStoreManager.getUser(context)
        userFlow.collect { user ->
            user?.let {
            }
            if (user != null) {
                callback(user)
            }
            }
    }

    suspend fun logout(context: Context,navController: NavController){
        DataStoreManager.clearToken(context)
        navController.navigate(Screen.LoginOrRegisterScreen.route){
            navController.popBackStack()
        }
    }


}