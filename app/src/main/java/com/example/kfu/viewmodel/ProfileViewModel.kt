package com.example.kfu.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.vectordrawable.animated.R
import com.example.kfu.dataclass.FileUpload
import com.example.kfu.dataclass.GetTypeList
import com.example.kfu.dataclass.ProfileResponse
import com.example.kfu.dataclass.UpdateProfile
import com.example.kfu.network.RetrofitProfile
import com.example.kfu.userInterface.ProfileApiService
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

    private val _userTypeList = MutableLiveData<GetTypeList>()
    val userTypeList: LiveData<GetTypeList> = _userTypeList

    private val _updateResponse = MutableLiveData<String?>()
    val updateResponse: LiveData<String?> = _updateResponse

    private val _updateError = MutableLiveData<String?>()
    val updateError: LiveData<String?> = _updateError



    fun fetchProfile(uuid: String) {
        val call = RetrofitProfile.instance.getProfile(uuid)
        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    _profileResponse.value = response.body()
//                    fetchUserTypes()
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
//        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
//        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
//
//        val call = RetrofitProfile.instance.uploadFile(body)
//        call.enqueue(object : Callback<FileUpload> {
//            override fun onResponse(call: Call<FileUpload>, response: Response<FileUpload>) {
//                if (response.isSuccessful) {
//                    fetchProfile(uuid)
//                } else {
//                    _profileError.value = "Image upload failed"
//                }
//            }
//
//            override fun onFailure(call: Call<FileUpload>, t: Throwable) {
//                _profileError.value = t.message
//            }
//        })
//    }


    fun fetchUserTypes() {
        RetrofitProfile.instance.getUserTypes().enqueue(object : Callback<GetTypeList> {
            override fun onResponse(call: Call<GetTypeList>, response: Response<GetTypeList>) {
                if (response.isSuccessful) {
                    _userTypeList.postValue(response.body())
                } else {
                    _profileError.postValue("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetTypeList>, t: Throwable) {
                _profileError.postValue(t.message)
            }
        })

    }


    fun updateProfile(context: Context, uuid: String, fullName: String, govtIdOrIqamaNo: String, userTypeId:Int, imageUri: Uri?) {
        val fullNamePart = RequestBody.create("text/plain".toMediaTypeOrNull(), fullName)
        val govtIdOrIqamaNoPart = RequestBody.create("text/plain".toMediaTypeOrNull(), govtIdOrIqamaNo)
        val userTypePart = RequestBody.create("text/plain".toMediaTypeOrNull(), userTypeId.toString())

        val imagePart: MultipartBody.Part? = imageUri?.let { uri ->
            val file = File(getRealPathFromURI(context, uri))
            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("image", file.name, requestFile)
        }

        val call = RetrofitProfile.instance.updateProfile(
            uuid,
            fullNamePart,
            govtIdOrIqamaNoPart,
            imagePart,
            userTypePart
        )
        call.enqueue(object : Callback<UpdateProfile> {
            override fun onResponse(call: Call<UpdateProfile>, response: Response<UpdateProfile>) {
                if (response.isSuccessful) {
                    _updateResponse.value = response.body().toString()
                } else {
                    _updateError.value = "Profile update failed"
                }
            }

            override fun onFailure(call: Call<UpdateProfile>, t: Throwable) {
                _updateError.value = "Exception: ${t.message}"
            }
        })
    }


    private fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor = context.contentResolver.query(contentUri, null, null, null, null)
        return if (cursor == null) {
            contentUri.path ?: ""
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val path = cursor.getString(idx)
            cursor.close()
            path
        }
    }
}