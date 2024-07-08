package com.example.kfu.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kfu.dataclass.LoginDataClass
import com.example.kfu.dataclass.LoginResponse
import com.example.kfu.network.RetrofitClient
import retrofit2.Call
import retrofit2.Response


class LoginViewModel : ViewModel() {


    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _loginError = MutableLiveData<String>()
    val loginError : LiveData<String> = _loginError

    fun loginUser(username: String, password: String) {
        val loginDataClass =LoginDataClass(password,username)
        RetrofitClient.instance.getLoginData(loginDataClass).enqueue(object :
            retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    _loginResponse.value = loginResponse!!
                } else {
                    _loginError.value = "Login failed"
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginError.value = "An error occurred"
            }

        })
    }
}