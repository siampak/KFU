package com.example.kfu.dataclass


import com.google.gson.annotations.SerializedName

data class GetTypeList(
    @SerializedName("data")
    var `data`: List<UserData?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("success")
    var success: Boolean?,
    @SerializedName("timestamp")
    var timestamp: String?
)
data class UserData(
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("updated_at")
    var updatedAt: String?,
    @SerializedName("user_type")
    var userType: String?
)