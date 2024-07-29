package com.example.kfu.userInterface

import com.example.kfu.dataclass.BrowseCourseList
import com.example.kfu.dataclass.CourseDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CourseApiService {

    @GET("course/browse-course-list/")
    suspend fun getBrowseCourseList(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): BrowseCourseList

    @GET("course/about/{id}/")
    suspend fun getCourseDetails(
        @Path("id") courseId: Int
    ): CourseDetails
}

