package com.example.kfu.dataclass


import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("data")
    var `data`: ProfileData?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("success")
    var success: Boolean?,
    @SerializedName("timestamp")
    var timestamp: String?
)

data class ProfileData(
    @SerializedName("cv")
    var cv: String?,
    @SerializedName("description")
    var description: Any?,
    @SerializedName("designation")
    var designation: Any?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("first_name")
    var firstName: String?,
    @SerializedName("full_name")
    var fullName: String?,
    @SerializedName("govt_id_or_iqama_no")
    var govtIdOrIqamaNo: String?,
    @SerializedName("groups")
    var groups: List<Any?>?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: Any?,
    @SerializedName("is_active")
    var isActive: Boolean?,
    @SerializedName("is_close_account")
    var isCloseAccount: Boolean?,
    @SerializedName("is_delete")
    var isDelete: Boolean?,
    @SerializedName("language_preferences")
    var languagePreferences: String?,
    @SerializedName("last_login")
    var lastLogin: Any?,
    @SerializedName("last_name")
    var lastName: String?,
    @SerializedName("phone_number")
    var phoneNumber: String?,
    @SerializedName("role")
    var role: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("user_permissions")
    var userPermissions: List<Any?>?,
    @SerializedName("user_type")
    var userType: Int?,
    @SerializedName("username")
    var username: String?,
    @SerializedName("uuid")
    var uuid: String?
)