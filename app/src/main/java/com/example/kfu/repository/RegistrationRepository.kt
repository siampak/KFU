package com.example.kfu.repository

import com.example.kfu.dataclass.Registration
import com.example.kfu.network.RetrofitRegistration
import retrofit2.Response

class RegistrationRepository {

    suspend fun registerUser(
        fullName: String,
        phoneNumber: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Response<Registration> {
        return RetrofitRegistration.api.registerUser(fullName, phoneNumber, email, password, confirmPassword)
    }
}