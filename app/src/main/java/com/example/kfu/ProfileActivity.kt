package com.example.kfu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kfu.databinding.ActivityProfileBinding
import com.example.kfu.network.RetrofitProfile
import com.example.kfu.viewmodel.ProfileViewModel


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)

        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)


        //Clicked on MyProfile
        binding.llMyProfile.setOnClickListener {
            navigateToMyProfileActivity()
        }


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


//            binding.profileImage.setOnClickListener {
//                showImagePickerOptions()
//            }

    }


    private fun observeViewModel() {
        profileViewModel.profileResponse.observe(this, Observer { profileResponse ->
            profileResponse?.data?.let { profile ->
                binding.profileName.text = profile.fullName
                val imageUrl = profile.image ?: ""
                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.profile_male)
                    .into(binding.profileImage)
            }
        })

        profileViewModel.profileError.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        profileViewModel.imageUri.observe(this, Observer { uri ->
            uri?.let {
                Glide.with(this).load(uri).into(binding.profileImage)
            }
        })
    }


    private fun navigateToMyProfileActivity() {
        val intent = Intent(this, MyProfileActivity::class.java)
        startActivity(intent)
    }
}














