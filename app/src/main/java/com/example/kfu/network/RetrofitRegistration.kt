package com.example.kfu.network

import com.example.kfu.userInterface.RegistrationApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitRegistration {
    private const val  BASE_URL = "https://kfu-dev-admin.ewnbd.com/api/v1/"

    val api: RegistrationApiService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RegistrationApiService::class.java)
    }
}