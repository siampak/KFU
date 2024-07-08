package com.example.kfu

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kfu.databinding.ActivityMyProfileBinding
import com.example.kfu.databinding.ActivityProfileBinding
import com.example.kfu.network.RetrofitProfile
import com.example.kfu.viewmodel.ProfileViewModel

class MyProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)


        RetrofitProfile.initializeSharedPreferences(this)
        //create "MyPrefs" folder in sharedPreference
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        //uuid te my prefs er data( uuid) save kora.
        val uuid = sharedPreferences.getString("uuid", null)

        //condition apply  null check korar jonno
        if (uuid != null) {
            observeViewModel()
            profileViewModel.fetchProfile(uuid)
        } else {
            Toast.makeText(this, "UUID not found", Toast.LENGTH_SHORT).show()
        }

    }


    private fun observeViewModel() {
        profileViewModel.profileResponse.observe(this, Observer { profileResponse ->
            profileResponse?.data?.let { profile ->
                binding.firstNameInput.setText(profile.firstName)
                binding.lastNameInput.setText(profile.lastName)
                binding.emailInput.setText(profile.email)
                binding.phoneNumberInput.setText(profile.phoneNumber)
                binding.typeOfUserInput.setText(profile.userType!!)

            }
        })

        profileViewModel.profileError.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

    }
}