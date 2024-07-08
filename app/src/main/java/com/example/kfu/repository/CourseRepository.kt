//package com.example.kfu.repository
//
//
//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.PagingData
//import com.example.kfu.dataclass.Course
//import com.example.kfu.network.CoursePagingSource
//import com.example.kfu.userInterface.CourseApiService
//import kotlinx.coroutines.flow.Flow
//
//class CourseRepository(private val apiService: CourseApiService) {
//    fun getCourses(): Flow<PagingData<Course>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 4,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { CoursePagingSource(apiService) }
//        ).flow
//    }
//}
//
//

//package com.example.kfu.repository
//
//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.PagingData
//import com.example.kfu.dataclass.Course
//import com.example.kfu.network.CoursePagingSource
//import com.example.kfu.userInterface.CourseApiService
//import kotlinx.coroutines.flow.Flow
//
//class CourseRepository(private val apiService: CourseApiService) {
//
//    fun getCourses(): Flow<PagingData<Course>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 10, // Adjust page size as needed
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { CoursePagingSource(apiService) }
//        ).flow
//    }
//}


import com.example.kfu.network.RetrofitCourse
import com.example.kfu.dataclass.BrowseCourseList

class CourseRepository {
    suspend fun getBrowseCourseList(page: Int, limit: Int): BrowseCourseList {
        return RetrofitCourse.api.getBrowseCourseList(page, limit)
    }
}


