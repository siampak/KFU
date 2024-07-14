// ProfileActivity.kt
package com.example.kfu

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kfu.databinding.ActivityProfileBinding
import com.example.kfu.network.RetrofitProfile
import com.example.kfu.viewmodel.ProfileViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var sharedPreferences: SharedPreferences


//    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val fileUri = result.data?.data
//            fileUri?.let { uri ->
//                profileViewModel.setImageUri(uri)
//                val file = File(getRealPathFromURI(uri))
//                val uuid = sharedPreferences.getString("uuid", null)
//                if (uuid != null) {
//                    profileViewModel.uploadProfileImage(uuid, file)
//                }
//            }
//        } else {
//            Toast.makeText(this, "Image selection failed", Toast.LENGTH_SHORT).show()
//        }
//    }

//    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val imageBitmap = result.data?.extras?.get("data") as Bitmap
//            val uri = saveBitmapToUri(imageBitmap)
//            uri?.let {
//                profileViewModel.setImageUri(it)
//                val file = File(it.path)
//                val uuid = sharedPreferences.getString("uuid", null)
//                if (uuid != null) {
//                    profileViewModel.setImageUri(it)
//                }
//            }
//        } else {
//            Toast.makeText(this, "Image capture failed", Toast.LENGTH_SHORT).show()
//        }
//    }


//    private val requestCameraPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//            if (isGranted) {
//                captureImageFromCamera()
//            } else {
//                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }

//    private val requestStoragePermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
//            if (isGranted) {
//                selectImageFromGallery()
//            } else {
//                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }

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

//    private fun selectImageFromGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        galleryLauncher.launch(intent)
//    }
//
//    private fun captureImageFromCamera() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        cameraLauncher.launch(intent)
//    }

//    private fun saveBitmapToUri(bitmap: Bitmap): Uri? {
////        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
////        val fileName = "JPEG_${timeStamp}_.jpg"
////        val storageDir: File? = getExternalFilesDir(null)
////        val file = File(storageDir, fileName)
//        val filesDir = filesDir
//        val imageFile = File(
//            filesDir,
//            "profile_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg"
//        )
//        return try {
//            val fos = FileOutputStream(imageFile)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//            fos.flush()
//            fos.close()
//            Uri.fromFile(imageFile)
//        } catch (e: IOException) {
//            e.printStackTrace()
//            null
//        }
//    }

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


    private fun navigateToMyProfileActivity() {
        val intent = Intent(this, MyProfileActivity::class.java)
        startActivity(intent)
    }
}














