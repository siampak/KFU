package com.example.kfu.userInterface

import com.example.kfu.dataclass.Registration
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegistrationApiService {

    @FormUrlEncoded
    @POST("user/registration/")
    suspend fun registerUser(
        @Field("full_name") fullName: String,
        @Field("phone_number") phoneNumber: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String,
        @Field("role") role: String = "TRAINEE"
    ): Response<Registration>
}