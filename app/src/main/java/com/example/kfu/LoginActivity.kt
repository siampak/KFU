package com.example.kfu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kfu.databinding.ActivityLoginBinding
import com.example.kfu.network.RetrofitClient
import com.example.kfu.network.RetrofitProfile
import com.example.kfu.viewmodel.LoginViewModel



class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel



    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        registrationClicked()
        setContentView(binding.root)
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        RetrofitProfile.initializeSharedPreferences(this)

        setStatusBarColor()
//        binding.btnLogin.setOnClickListener{
//            login()
//        }

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]


        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        binding.btnLogin.setOnClickListener {
            val username = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            loginUser(username, password)

        }

        viewModel.loginResponse.observe(this) { response ->
            response?.let {
                saveTokenToSharedPreferences(it.accessToken, it.data?.uuid)
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                navigateToHomeActivity()
            }
        }

        viewModel.loginError.observe(this) { error ->
            error?.let {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun loginUser(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.loginUser(username, password)
    }


    private fun saveTokenToSharedPreferences(token: String?, uuid: String?) {
        token?.let {
            with(sharedPreferences.edit()) {
                putString("TOKEN", it)
                apply()
            }
        }
        uuid?.let {
            with(sharedPreferences.edit()){
                putString("uuid", it)
                apply()
            }
        }
    }

//    private fun navigateToHomeActivity() {
//        val intent = Intent(this, HomeActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
    private fun navigateToHomeActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun registrationClicked() {
        binding.tvRegistration.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    //StatusBar color change code for this activity(Activity)
    private fun setStatusBarColor() {
        window?.apply {
            val statusBarColors= ContextCompat.getColor(this@LoginActivity, R.color.blue_dark)
            statusBarColor=statusBarColors
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M ){
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }


}





//    private fun login (){
//        val username= binding.etEmail.text.toString().trim()
//        val password = binding.etPassword.text.toString().trim()
//        val loginDataClass =LoginDataClass(password,username)
//
//        if (username.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        RetrofitClient.instance.getLoginData(loginDataClass).enqueue(object : Callback<LoginResponse>{
//            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//
//                if(response.isSuccessful){
//                    val loginResponse = response.body()
//                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
//                }
//                else{
//                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
//
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                Toast.makeText(this@LoginActivity, "An error occurred", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//    }



//    private fun loginClicked() {
//        binding.btnLogin.setOnClickListener {
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//        }
//    }




