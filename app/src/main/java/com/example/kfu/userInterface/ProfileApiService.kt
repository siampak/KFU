package com.example.kfu.userInterface

import com.example.kfu.dataclass.FileUpload
import com.example.kfu.dataclass.GetTypeList
import com.example.kfu.dataclass.ProfileResponse
import com.example.kfu.dataclass.UpdateData
import com.example.kfu.dataclass.UpdateProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileApiService {
        @GET("user/profile/{uuid}/")
        fun getProfile(@Path("uuid") uuid: String): Call<ProfileResponse>

       // use upload Api
//        @Multipart
//        @POST("upload/")
//        fun uploadFile(
//                @Part file: MultipartBody.Part
//        ): Call<FileUpload>



        @GET("user/user-type/")
        fun getUserTypes(): Call<GetTypeList>


        @Multipart
        @PUT("user/profile/{uuid}/")
        fun updateProfile(
                @Path("uuid") uuid: String,
                @Part("full_name") fullName: RequestBody,
                @Part("govt_id_or_iqama_no") govtIdOrIqamaNo: RequestBody,
                @Part image: MultipartBody.Part?,
                @Part("user_type") userType: RequestBody
        ): Call<UpdateProfile>

//        @Multipart
//        @PUT("user/profile/{uuid}")
//        fun updateProfile(@Path("uuid") uuid: String, @Part("data") updateData: RequestBody): Call<UpdateProfile>

}