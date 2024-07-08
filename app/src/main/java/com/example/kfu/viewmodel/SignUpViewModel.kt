package com.example.kfu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kfu.dataclass.Registration
import com.example.kfu.repository.RegistrationRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    private val registrationRepository = RegistrationRepository()

    private val _registrationResponse = MutableLiveData<Registration?>()
    val registrationResponse: LiveData<Registration?>
        get() = _registrationResponse

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun registerUser(fullName: String, phoneNumber: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            try {
                val response = registrationRepository.registerUser(fullName, phoneNumber, email, password, confirmPassword)
                handleResponse(response)
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    private fun handleResponse(response: Response<Registration>) {
        if (response.isSuccessful) {
            _registrationResponse.value = response.body()
        } else {
            _errorMessage.value = response.errorBody()?.string()
        }
    }
}



//package com.example.kfu.viewmodel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.kfu.dataclass.Registration
//import com.example.kfu.repository.RegistrationRepository
//import kotlinx.coroutines.launch
//
//class SignUpViewModel : ViewModel() {
//
//    private val registrationRepository = RegistrationRepository()
//
//    private val _registrationResponse = MutableLiveData<Registration>()
//    val registrationResponse: LiveData<Registration>
//        get() = _registrationResponse
//
//    private val _errorMessage = MutableLiveData<String>()
//    val errorMessage: LiveData<String>
//        get() = _errorMessage
//
//    fun registerUser(fullName: String, phoneNumber: String, email: String, password: String, confirmPassword: String) {
//        viewModelScope.launch {
//            try {
//                val response = registrationRepository.registerUser(fullName, phoneNumber, email, password, confirmPassword)
//                if (response.isSuccessful) {
//                    _registrationResponse.value = response.body()
//                } else {
//                    _errorMessage.value = response.errorBody()?.string()
//                }
//            } catch (e: Exception) {
//                _errorMessage.value = e.message
//            }
//        }
//    }
//}


