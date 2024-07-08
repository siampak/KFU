package com.example.kfu

import CourseAdapter
import CourseRepository
import CourseViewModel
import android.os.Build
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kfu.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val repository = CourseRepository()
//    private val viewModel: CourseViewModel by viewModels { CourseViewModel.Factory(repository) }
    private val viewModel: CourseViewModel by lazy {
       ViewModelProvider(this, CourseViewModel.Factory(repository))
            .get(CourseViewModel::class.java)
    }
    private lateinit var courseAdapter: CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColor()

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        courseAdapter = CourseAdapter(mutableListOf())
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = courseAdapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.fetchCourses()
                }
            }
        })
    }

    private fun setupObservers() {
        viewModel.courses.observe(this, Observer { courses ->
            courseAdapter.addCourses(courses)
        })
    }

    private fun setStatusBarColor() {
        window?.apply {
            val statusBarColors = ContextCompat.getColor(this@HomeActivity, R.color.blue_dark)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}



//package com.example.kfu
//import CourseAdapter
//import android.content.ContentValues.TAG
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.lifecycleScope
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.kfu.databinding.ActivityHomeBinding
//import com.example.kfu.network.RetrofitCourse
//import com.example.kfu.repository.CourseRepository
//import com.example.kfu.viewmodel.CourseViewModel
//import com.example.kfu.viewmodel.CourseViewModelFactory
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//
//class HomeActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityHomeBinding
//    private lateinit var courseAdapter: CourseAdapter
//
//    private val viewModel: CourseViewModel by lazy {
//        ViewModelProvider(this, CourseViewModelFactory(CourseRepository(RetrofitCourse.api)))
//            .get(CourseViewModel::class.java)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityHomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        setStatusBarColor()
//
//        setupRecyclerView()
//        setupObservers()
//
//        binding.tvBrowse.setOnClickListener {
//            fetchCourses()
//        }
//    }
//
//    private fun setupRecyclerView() {
//        courseAdapter = CourseAdapter()
//        binding.recyclerView.apply {
//            layoutManager = GridLayoutManager(this@HomeActivity, 2)
//            adapter = courseAdapter
//            addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//                    if (!recyclerView.canScrollVertically(1)) {
//                        fetchCourses()
//                    }
//                }
//            })
//        }
//    }
//
//    private fun setupObservers() {
//        lifecycleScope.launch {
//            viewModel.courseList.collectLatest { pagingData ->
//                try {
//                    courseAdapter.submitData(pagingData)
//                } catch (e: Exception) {
//                    Log.e(TAG, "Error submitting data: ${e.message}", e)
//
//                }
//            }
//        }
//    }
//
//    private fun fetchCourses() {
//    }
//
//    private fun setStatusBarColor() {
//        window?.apply {
//            val statusBarColors = ContextCompat.getColor(this@HomeActivity, R.color.blue_dark)
//            statusBarColor = statusBarColors
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            }
//        }
//    }
//}
