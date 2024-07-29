import com.example.kfu.network.RetrofitCourse
import com.example.kfu.dataclass.BrowseCourseList
import com.example.kfu.dataclass.CourseDetails

class CourseRepository {
    suspend fun getBrowseCourseList(page: Int, limit: Int): BrowseCourseList {
        return RetrofitCourse.api.getBrowseCourseList(page, limit)
    }

    suspend fun getCourseDetails(courseId: Int): CourseDetails {
        return RetrofitCourse.api.getCourseDetails(courseId)
    }
}


