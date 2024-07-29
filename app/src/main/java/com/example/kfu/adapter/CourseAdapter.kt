import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kfu.databinding.ItemCardBinding
import com.example.kfu.dataclass.Course

class CourseAdapter(
    private val courses: MutableList<Course>,
    private val onCourseClicked: (Int) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

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

//            root.setOnClickListener {
//                course.courseImage.let { id -> onCourseClicked(id as Int) }
//            }

            root.setOnClickListener {
                onCourseClicked(course.id)  // Use course.id directly
            }
        }
    }


    override fun getItemCount() = courses.size

    fun addCourses(newCourses: List<Course>) {
        val startPosition = courses.size
        courses.addAll(newCourses)
        notifyItemRangeInserted(startPosition, newCourses.size)
    }
}



