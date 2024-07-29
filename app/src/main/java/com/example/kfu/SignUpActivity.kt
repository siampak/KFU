// SignUpActivity.kt
package com.example.kfu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kfu.viewmodel.SignUpViewModel
import com.example.kfu.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            val fullName = binding.etFullName.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            signUpViewModel.registerUser(fullName, phoneNumber, email, password, confirmPassword)
        }

        signUpViewModel.registrationResponse.observe(this, Observer { registration ->
            registration?.let {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
        })

        signUpViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        })

        binding.tvLoginLink.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
