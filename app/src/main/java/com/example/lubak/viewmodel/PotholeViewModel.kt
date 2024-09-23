package com.example.lubak.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.lubak.api.RetrofitClient
import com.example.lubak.model.PotholeModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PotholeViewModel : ViewModel() {
    var pothole by   mutableStateOf<PotholeModel?>(null)

     fun fetchPothole(id: Int){
        RetrofitClient.instance.getPotholeDetails(id).enqueue(object : Callback<PotholeModel> {
            override fun onResponse(call: Call<PotholeModel>, response: Response<PotholeModel>) {
                if (response.isSuccessful) {
                    pothole = response.body()

                } else {
                    pothole =null
                }
            }

            override fun onFailure(call: Call<PotholeModel>, t: Throwable) {
                pothole =null
            }
        })
    }

}
