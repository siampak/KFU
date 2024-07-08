//package com.example.kfu.network
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.kfu.dataclass.Course
//import com.example.kfu.userInterface.CourseApiService
//
//class CoursePagingSource(private val apiService: CourseApiService) : PagingSource<Int, Course>() {
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Course> {
//        return try {
//            val nextPage = params.key ?: 1
//            val response = apiService.getBrowseCourseList(nextPage, params.loadSize)
//            val courses = response.items?.map {
//                Course(
//                    courseImage = it?.coverImage ?: "",
//                    profileImage = it?.createdBy?.image ?: "",
//                    profileName = it?.createdBy?.fullName ?: "",
//                    courseTitle = it?.title ?: ""
//                )
//            } ?: emptyList()
//
//            LoadResult.Page(
//                data = courses,
//                prevKey = if (nextPage == 1) null else nextPage - 1,
//                nextKey = if (courses.isEmpty()) null else nextPage + 1
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Course>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//}



//package com.example.kfu.network
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.kfu.dataclass.Course
//import com.example.kfu.userInterface.CourseApiService
//import retrofit2.HttpException
//import java.io.IOException
//
//class CoursePagingSource(private val apiService: CourseApiService) : PagingSource<Int, Course>() {
//
//    companion object {
//        const val STARTING_PAGE_INDEX = 1
//        const val MAX_PAGES = 11 // Maximum pages to load
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Course> {
//        return try {
//            val nextPage = params.key ?: STARTING_PAGE_INDEX
//            if (nextPage > MAX_PAGES) {
//                return LoadResult.Error(Exception("Reached maximum pages"))
//            }
//
//            val response = apiService.getBrowseCourseList(nextPage, params.loadSize)
//            val courses = response.items?.map {
//                Course(
//                    courseImage = it?.coverImage ?: "",
//                    profileImage = it?.createdBy?.image ?: "",
//                    profileName = it?.createdBy?.fullName ?: "",
//                    courseTitle = it?.title ?: ""
//                )
//            } ?: emptyList()
//
//            LoadResult.Page(
//                data = courses,
//                prevKey = if (nextPage == STARTING_PAGE_INDEX) null else nextPage - 1,
//                nextKey = if (courses.isEmpty()) null else nextPage + 1
//            )
//        } catch (e: IOException) {
//            LoadResult.Error(e)
//        } catch (e: HttpException) {
//            if (e.code() == 400 && nextPage == MAX_PAGES) {
//                // Handle bad request error for the last page
//                LoadResult.Error(Exception("Reached maximum pages"))
//            } else {
//                LoadResult.Error(e)
//            }
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Course>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//}


package com.example.kfu.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.kfu.dataclass.Course
import com.example.kfu.userInterface.CourseApiService
import retrofit2.HttpException
import java.io.IOException

class CoursePagingSource(private val apiService: CourseApiService) : PagingSource<Int, Course>() {

    companion object {
        const val STARTING_PAGE_INDEX = 1
        const val MAX_PAGES = 11 // Maximum pages to load
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Course> {
        return try {
            val nextPage = params.key ?: STARTING_PAGE_INDEX
            if (nextPage > MAX_PAGES) {
                return LoadResult.Error(Exception("Reached maximum pages"))
            }

            val response = apiService.getBrowseCourseList(nextPage, params.loadSize)
            val courses = response.items?.map {
                Course(
                    courseImage = it?.coverImage ?: "",
                    profileImage = it?.createdBy?.image ?: "",
                    profileName = it?.createdBy?.fullName ?: "",
                    courseTitle = it?.title ?: ""
                )
            } ?: emptyList()

            LoadResult.Page(
                data = courses,
                prevKey = if (nextPage == STARTING_PAGE_INDEX) null else nextPage - 1,
                nextKey = if (courses.isEmpty()) null else nextPage + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
//        } catch (e: HttpException) {
////            if (e.code() == 400 && nextPage == MAX_PAGES) {
////                // Handle bad request error for the last page
////                LoadResult.Error(Exception("Reached maximum pages"))
////            } else {
////                LoadResult.Error(e)
////            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Course>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}


