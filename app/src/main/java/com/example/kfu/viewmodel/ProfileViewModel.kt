package com.example.kfu.viewmodel

import android.content.Context
import android.net.Uri
import android.os.FileUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kfu.dataclass.FileUpload
import com.example.kfu.dataclass.ProfileResponse
import com.example.kfu.network.RetrofitProfile
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileViewModel : ViewModel() {
    private val _profileResponse = MutableLiveData<ProfileResponse>()
    val profileResponse: LiveData<ProfileResponse> = _profileResponse

    private val _profileError = MutableLiveData<String>()
    val profileError: LiveData<String> = _profileError

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> = _imageUri

//    fun fetchProfile(uuid: String) {
//      val call =  RetrofitProfile.instance.getProfile(uuid)
//          call.enqueue(object : Callback<ProfileResponse> {
//            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
//                if (response.isSuccessful) {
//                    _profileResponse.value = response.body()
//                } else {
//                    _profileError.value = "Failed to fetch profile data"
//                }
//            }
//
//            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
//                _profileError.value = "An error occurred: ${t.message}"
//            }
//        })
//    }


    fun fetchProfile(uuid: String) {
        val call = RetrofitProfile.instance.getProfile(uuid)
        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    _profileResponse.value = response.body()
                } else {
                    _profileError.value = "Failed to fetch profile"
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _profileError.value = t.message
            }
        })
    }

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }


//    fun uploadProfileImage(uuid: String, file: File) {
//        viewModelScope.launch {
//            try {
//                val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
//                val uuidPart = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), uuid)
//
//                val response = RetrofitProfile.instance.uploadFile(body, uuidPart)
//                if (response.isSuccessful) {
//                   fetchProfile(uuid)
//                } else {
//                    _profileError.postValue("Error: ${response.message()}")
//                }
//            } catch (e: Exception) {
//                _profileError.postValue("Exception: ${e.message}")
//            }
//        }
//    }


    fun uploadProfileImage(uuid: String, file: File) {
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val call = RetrofitProfile.instance.uploadFile(body)
        call.enqueue(object : Callback<FileUpload> {
            override fun onResponse(call: Call<FileUpload>, response: Response<FileUpload>) {
                if (response.isSuccessful) {
                    fetchProfile(uuid)
                } else {
                    _profileError.value = "Image upload failed"
                }
            }

            override fun onFailure(call: Call<FileUpload>, t: Throwable) {
                _profileError.value = t.message
            }
        })
    }
}