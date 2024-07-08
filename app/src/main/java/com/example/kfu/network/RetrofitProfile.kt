package com.example.kfu.network

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.kfu.userInterface.ProfileApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProfile {

    private const val BASE_URL = "https://kfu-dev-admin.ewnbd.com/api/v1/"

    private lateinit var sharedPreferences: SharedPreferences

    fun initializeSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    private val client by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${getAccessToken()}")
                    .build()
                chain.proceed(request)
            }
            .build()
    }


    private fun getAccessToken(): String {
        return sharedPreferences.getString("TOKEN", "") ?: ""
    }

    val instance: ProfileApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ProfileApiService::class.java)
    }

}



//    private val logging = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }
//
//    private val client = OkHttpClient.Builder()
//        .addInterceptor(logging)
//        .build()
//
//    val instance: ProfileApiService by lazy {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//
//        retrofit.create(ProfileApiService::class.java)
//    }
//}