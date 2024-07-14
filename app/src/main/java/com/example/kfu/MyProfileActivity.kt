package com.example.kfu

import android.Manifest
import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kfu.databinding.ActivityMyProfileBinding
import com.example.kfu.dataclass.UserData
import com.example.kfu.network.RetrofitProfile
import com.example.kfu.viewmodel.ProfileViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MyProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private var userTypeList: List<UserData?>? = null
//    private var selectedImageFile: File? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val fileUri = result.data?.data
            fileUri?.let { uri ->
                profileViewModel.setImageUri(uri)
//                val file = File(getRealPathFromURI(uri))
//                val uuid = sharedPreferences.getString("uuid", null)
//                if (uuid != null) {
//                    profileViewModel.uploadProfileImage(uuid, file)
//                }
            }
        } else {
            Toast.makeText(this, "Image selection failed", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            val uri = saveBitmapToUri(imageBitmap)
            uri?.let {
                profileViewModel.setImageUri(it)
//                val file = File(it.path)
//                val uuid = sharedPreferences.getString("uuid", null)
//                if (uuid != null) {
//                    profileViewModel.uploadProfileImage(uuid, file)
//                }
            }
        } else {
            Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show()
        }
    }


    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                captureImageFromCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                selectImageFromGallery()
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }

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
            //ll
            profileViewModel.fetchUserTypes()
        } else {
            Toast.makeText(this, "UUID not found", Toast.LENGTH_SHORT).show()
        }

        binding.typeOfUserInput.setOnClickListener {
            binding.typeOfUserInput.showDropDown()
        }


        binding.profileImage.setOnClickListener {
            showImagePickerDialog()
        }

        binding.updateDetailsButton.setOnClickListener {
            val fullName = binding.fullNameInput.text.toString()
            val govtId = binding.govtIdInput.text.toString()
            val selectedUserType = binding.typeOfUserInput.text.toString()// Assuming this is where user selects or types user type
            val userTypeId = userTypeList?.find { it?.userType == selectedUserType }?.id ?: 0
            if (uuid != null) {

                profileViewModel.updateProfile(
                    this,
                    uuid,
                    fullName,
                    govtId,
//                    selectedUserType,
                    userTypeId,
                    profileViewModel.imageUri.value
                )
            }
        }
    }


    private fun observeViewModel() {
        profileViewModel.profileResponse.observe(this, Observer { profileResponse ->

            profileResponse?.data?.let { profile ->
                Glide.with(this)
                    .load(profile.image)

                    .into(binding.profileImage)

                binding.profileName.text = profile.fullName
                binding.fullNameInput.setText(profile.fullName)
                binding.emailInput.setText(profile.email)
                binding.phoneNumberInput.setText(profile.phoneNumber)
                binding.govtIdInput.setText(profile.govtIdOrIqamaNo)



                profile.userType?.let { userType ->
                    profileViewModel.userTypeList.observe(this, Observer { userTypeList ->
                        this.userTypeList =userTypeList.data
                        userTypeList?.data?.forEach { userData ->
                            if (userData?.id == userType) {  ///pppppppppppp
                                binding.typeOfUserInput.setText(userData.userType)
                                return@forEach // use kora hoyse -->  for Exit loop after finding the match.
                            }
                        }

                        val userTypeNames = userTypeList.data?.map { it?.userType.orEmpty() } ?: listOf()
                        val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, userTypeNames)
                        binding.typeOfUserInput.setAdapter(adapter)

                    })
                }
            }

        })

        //now observe user type list from api
//            profileViewModel.userTypeList.observe(this, Observer { userTypeList ->
//                val adapter = ArrayAdapter(
//                    this,
//                    android.R.layout.simple_spinner_item,
//                    userTypeList.data ?: emptyList()
//                )
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                binding.typeOfUserInput.setAdapter(adapter)
//
//                val selectedType = userTypeList.data?.firstOrNull { it?.id == profileViewModel.profileResponse.value?.data?.userType }
//                binding.typeOfUserInput.setSelection(adapter.getPosition(selectedType))


        profileViewModel.imageUri.observe(this, Observer { uri ->
            Glide.with(this)
                .load(uri)
                .into(binding.profileImage)
        })

        profileViewModel.updateResponse.observe(this, Observer { updateResponse ->
            updateResponse?.let {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            }
        })

        profileViewModel.updateError.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, "Profile update failed: $it", Toast.LENGTH_SHORT).show()
            }
        })
}


//    private fun observeViewModel() {
//        profileViewModel.profileResponse.observe(this, Observer { profileResponse ->
//            profileResponse?.data?.let { profile ->
//
//                //glide image
//                val imageUrl = profile.image ?: ""
//                Glide.with(this)
//                    .load(imageUrl)
////                    .placeholder(com.example.kfu.R.drawable.profile_male)
//                    .into(binding.profileImage)
//
//                binding.fullNameInput.setText(profile.fullName)
//                binding.emailInput.setText(profile.email)
//                binding.phoneNumberInput.setText(profile.phoneNumber)
//                binding.govtIdInput.setText(profile.govtIdOrIqamaNo)
//
//
//                profile.userType?.let { userType ->
//                    profileViewModel.userTypeList.observe(this, Observer { userTypeList ->
//                        userTypeList?.data?.forEach { userData ->
//                            if (userData?.id == userType) {
//                                binding.typeOfUserInput.setText(userData.userType)
//                                return@forEach // use kora hoyse -->  for Exit loop after finding the match.
//                            }
//                        }
//                        // Setup AutoCompleteTextView Adapter
//                        val userTypeNames = userTypeList.data?.map { it?.userType.orEmpty() } ?: listOf()
//                        val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, userTypeNames)
//                        binding.typeOfUserInput.setAdapter(adapter)
//
////
//                    })
//                }
//            }
//        })
//
//        //glide image
//        profileViewModel.imageUri.observe(this, Observer { uri ->
//            uri?.let {
//                Glide.with(this).load(uri).into(binding.profileImage)
//            }
//        })
//
//
//        profileViewModel.profileError.observe(this, Observer { error ->
//            error?.let {
//                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
//            }
//        })
//
//        profileViewModel.updateResponse.observe(this, Observer { updateResponse ->
//            updateResponse?.let {
//                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
//            }
//        })
//        profileViewModel.updateError.observe(this, Observer { error ->
//            error?.let {
//                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
//            }
//        })
//
//    }



    private fun showImagePickerDialog() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Choose your profile picture")

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> {
                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
                options[item] == "Choose from Gallery" -> {
                    requestStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }



//    private fun showImagePickerOptions() {
//
//        val options = arrayOf("Select from Gallery", "Capture from Camera")
//        val builder = android.app.AlertDialog.Builder(this)
//        builder.setTitle("Choose an option")
//        builder.setItems(options) { dialog, which ->
//            when (which) {
//                0 -> requestStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                1 -> requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
//            }
//
//        }
//        builder.show()
//
//    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun captureImageFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    //glide image
    private fun saveBitmapToUri(bitmap: Bitmap): Uri? {
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
//        val fileName = "JPEG_${timeStamp}_.jpg"
//        val storageDir: File? = getExternalFilesDir(null)
//        val file = File(storageDir, fileName)
        val filesDir = filesDir
        val imageFile = File(
            filesDir,
            "profile_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg"
        )
        return try {
            val fos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            Uri.fromFile(imageFile)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

//    //glide image
//    private fun getRealPathFromURI(contentUri: Uri): String? {
//        var cursor = contentResolver.query(contentUri, null, null, null, null)
//        return if (cursor == null) {
//            contentUri.path
//        } else {
//            cursor.moveToFirst()
//            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
//            cursor.getString(idx)
//        }
//    }
}

