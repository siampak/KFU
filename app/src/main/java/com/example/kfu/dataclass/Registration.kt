package com.example.kfu.dataclass


import com.google.gson.annotations.SerializedName

data class Registration(
    @SerializedName("data")
    var `data`: RData?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("success")
    var success: Boolean?,
    @SerializedName("timestamp")
    var timestamp: String?
)


data class RData(
    @SerializedName("email")
    var email: String?,
    @SerializedName("full_name")
    var fullName: String?,
    @SerializedName("phone_number")
    var phoneNumber: String?,
    @SerializedName("role")
    var role: String?
)