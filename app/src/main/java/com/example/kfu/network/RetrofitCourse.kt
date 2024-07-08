package com.example.kfu.network

import com.example.kfu.userInterface.CourseApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCourse {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://kfu-dev-admin.ewnbd.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: CourseApiService by lazy {
        retrofit.create(CourseApiService::class.java)
    }
}

