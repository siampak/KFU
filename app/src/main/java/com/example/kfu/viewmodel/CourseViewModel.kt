//package com.example.kfu.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewModelScope
//import androidx.paging.PagingData
//import androidx.paging.cachedIn
//import com.example.kfu.dataclass.Course
//import com.example.kfu.repository.CourseRepository
//
//import kotlinx.coroutines.flow.Flow
//
//class CourseViewModel(private val repository: CourseRepository) : ViewModel() {
//    val courseList: Flow<PagingData<Course>> = repository.getCourses().cachedIn(viewModelScope)
//}
//
//class CourseViewModelFactory(private val repository: CourseRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return CourseViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}


import androidx.lifecycle.*
import com.example.kfu.dataclass.Course
import kotlinx.coroutines.launch

class CourseViewModel(private val repository: CourseRepository) : ViewModel() {
    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> get() = _courses

    private var currentPage = 1
    private var isLoading = false
    private var totalPageCount = Int.MAX_VALUE

    init {
        fetchCourses()
    }

     fun fetchCourses() {
        if (isLoading || currentPage > totalPageCount) return

        isLoading = true
        viewModelScope.launch {
            try {
                val response = repository.getBrowseCourseList(page = currentPage,  4)
                val newCourses = response.items?.filterNotNull()?.map { item ->
                    Course(
                        courseImage = item.coverImage ?: "",
                        profileImage = item.createdBy?.image ?: "",
                        profileName = item.createdBy?.fullName ?: "",
                        courseTitle = item.title ?: ""
                    )
                } ?: emptyList()

                val currentList = _courses.value.orEmpty()
                _courses.value = currentList + newCourses

                totalPageCount = response.metadata?.pagination?.pageCount ?: Int.MAX_VALUE
                currentPage++
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    class Factory(private val repository: CourseRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CourseViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


