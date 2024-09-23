package com.example.lubak.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.lubak.api.RetrofitClient
import com.example.lubak.model.PotholeModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    var potholes by   mutableStateOf<List<PotholeModel>?>(null)
    fun fetchPotholes() {
        RetrofitClient.instance.getAllPotholes().enqueue(object : Callback<List<PotholeModel>> {
            override fun onResponse(call: Call<List<PotholeModel>>, response: Response<List<PotholeModel>>) {
                if (response.isSuccessful) {
                    potholes = response.body()

                } else {
                    potholes =null
                }
            }

            override fun onFailure(call: Call<List<PotholeModel>>, t: Throwable) {
                potholes =null
            }
        })
    }
}
