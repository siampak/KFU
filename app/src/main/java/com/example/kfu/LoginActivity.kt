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
import androidx.lifecycle.ViewModelProvider
import com.example.kfu.databinding.ActivityLoginBinding
import com.example.kfu.network.RetrofitProfile
import com.example.kfu.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        registrationClicked()
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        RetrofitProfile.initializeSharedPreferences(this)

        setStatusBarColor()

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]



        binding.btnLogin.setOnClickListener {
            val username = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            loginUser(username, password)

        }

        loginViewModel.loginResponse.observe(this) { response ->
            response?.let {
                saveTokenToSharedPreferences(it.accessToken, it.data?.uuid)
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                navigateToHomeActivity()
            }
        }

        loginViewModel.loginError.observe(this) { error ->
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
        loginViewModel.loginUser(username, password)
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

    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
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




