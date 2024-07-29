package com.example.kfu

import CourseAdapter
import CourseRepository
import com.example.kfu.viewmodel.CourseViewModel
import android.content.Intent
import android.content.SharedPreferences
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
//    private val viewModel: com.example.kfu.viewmodel.CourseViewModel by viewModels { com.example.kfu.viewmodel.CourseViewModel.Factory(repository) }
    private val viewModel: CourseViewModel by lazy {
       ViewModelProvider(this, CourseViewModel.Factory(repository))
            .get(CourseViewModel::class.java)
    }
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColor()
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        courseAdapter = CourseAdapter(mutableListOf()) { courseId ->
            navigateToCourseDetails(courseId)
        }
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

    private fun navigateToCourseDetails(courseId: Int) {
        val intent = Intent(this, CourseDetailsPageActivity::class.java)
        intent.putExtra("COURSE_ID", courseId)
        startActivity(intent)
    }



}
