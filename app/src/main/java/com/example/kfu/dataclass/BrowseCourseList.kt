package com.example.kfu.dataclass


import com.google.gson.annotations.SerializedName

data class BrowseCourseList(
    @SerializedName("items")
    var items: List<Item?>?,
    @SerializedName("message")
    var message: String?,
    @SerializedName("metadata")
    var metadata: Metadata?,
    @SerializedName("timestamp")
    var timestamp: String?
)

data class CreatedBy(
    @SerializedName("description")
    var description: Any?,
    @SerializedName("designation")
    var designation: Any?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("first_name")
    var firstName: Any?,
    @SerializedName("full_name")
    var fullName: String?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("last_name")
    var lastName: Any?
)

data class Item(
    @SerializedName("availability")
    var availability: Any?,
    @SerializedName("category")
    var category: Int?,
    @SerializedName("cover_image")
    var coverImage: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("created_by")
    var createdBy: CreatedBy?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("facilitator")
    var facilitator: List<Int?>?,
    @SerializedName("fee")
    var fee: Double?,
    @SerializedName("group_enrolled_discount_fee")
    var groupEnrolledDiscountFee: Double?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("qr_code")
    var qrCode: String?,
    @SerializedName("registration_status")
    var registrationStatus: String?,
    @SerializedName("slug")
    var slug: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("subtitle")
    var subtitle: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("updated_at")
    var updatedAt: String?
)

data class Metadata(
    @SerializedName("pagination")
    var pagination: Pagination?
)


data class Pagination(
    @SerializedName("currentPage")
    var currentPage: Int?,
    @SerializedName("limit")
    var limit: Int?,
    @SerializedName("nextOffset")
    var nextOffset: Int?,
    @SerializedName("offset")
    var offset: Int?,
    @SerializedName("pageCount")
    var pageCount: Int?,
    @SerializedName("previousOffset")
    var previousOffset: Int?,
    @SerializedName("totalCount")
    var totalCount: Int?
)