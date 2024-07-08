package com.example.kfu.userInterface

import com.example.kfu.dataclass.FileUpload
import com.example.kfu.dataclass.ProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileApiService {
        @GET("user/profile/{uuid}/")
        fun getProfile(@Path("uuid") uuid: String): Call<ProfileResponse>


        @Multipart
        @POST("upload/")
        fun uploadFile(
                @Part file: MultipartBody.Part
        ): Call<FileUpload>

//        @Multipart
//        @POST("upload/")
//        suspend fun uploadFile(
//                @Part file: MultipartBody.Part,
//                @Part("uuid") uuid: RequestBody
//        ): Response<ProfileResponse>


}