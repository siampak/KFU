package com.example.kfu.userInterface


import com.example.kfu.dataclass.LoginDataClass
import com.example.kfu.dataclass.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    @POST("user/login/")
    fun getLoginData(
        @Body loginDataClass: LoginDataClass
    ): Call<LoginResponse>

}
