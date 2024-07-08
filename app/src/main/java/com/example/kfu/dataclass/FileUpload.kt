package com.example.kfu.dataclass


import com.google.gson.annotations.SerializedName

data class FileUpload(
    @SerializedName("file")
    var `file`: List<String?>?
)