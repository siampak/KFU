package com.example.kfu.userInterface

import com.example.kfu.dataclass.BrowseCourseList
import retrofit2.http.GET
import retrofit2.http.Query

interface CourseApiService {

    @GET("course/browse-course-list/")
    suspend fun getBrowseCourseList(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): BrowseCourseList
}

