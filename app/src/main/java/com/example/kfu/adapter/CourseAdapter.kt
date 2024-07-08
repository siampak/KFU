import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kfu.databinding.ItemCardBinding
import com.example.kfu.dataclass.Course

class CourseAdapter(private val courses: MutableList<Course>) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    inner class CourseViewHolder(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        with(holder.binding) {
            tvCourseTitle.text = course.courseTitle
            tvProfileName.text = course.profileName

            Glide.with(root.context)
                .load(course.courseImage)
                .into(ivCourseImage)

            Glide.with(root.context)
                .load(course.profileImage)
                .into(ivProfileImage)
        }
    }

    override fun getItemCount() = courses.size

    fun addCourses(newCourses: List<Course>) {
        val startPosition = courses.size
        courses.addAll(newCourses)
        notifyItemRangeInserted(startPosition, newCourses.size)
    }
}



//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.paging.PagingDataAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.kfu.databinding.ItemCardBinding
//import com.example.kfu.dataclass.Course
//
//class CourseAdapter : PagingDataAdapter<Course, CourseAdapter.CourseViewHolder>(DiffCallback) {
//
//    inner class CourseViewHolder(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
//        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return CourseViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
//        val course = getItem(position)
//        course?.let {
//            with(holder.binding) {
//                tvCourseTitle.text = it.courseTitle
//                tvProfileName.text = it.profileName
//
//                Glide.with(root.context)
//                    .load(it.courseImage)
//                    .into(ivCourseImage)
//
//                Glide.with(root.context)
//                    .load(it.profileImage)
//                    .into(ivProfileImage)
//            }
//        }
//    }
//
//    companion object {
//        private val DiffCallback = object : DiffUtil.ItemCallback<Course>() {
//            override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
//                return oldItem.courseTitle == newItem.courseTitle // Adjust this as needed
//            }
//
//            override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//}


