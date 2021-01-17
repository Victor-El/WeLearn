package me.codeenzyme.welearn.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.squareup.picasso.Picasso
import me.codeenzyme.welearn.R
import me.codeenzyme.welearn.databinding.CourseItemLayoutBinding
import me.codeenzyme.welearn.model.Course
import java.text.SimpleDateFormat

class CoursesRecyclerViewAdapter(
    val context: Context,
    private val onCourseItemClick: OnCourseItemClick
    ) : RecyclerView.Adapter<CoursesRecyclerViewAdapter.CoursesViewHolder>() {

    private val courses = mutableListOf<Course>()

    private lateinit var _binding: CourseItemLayoutBinding

    inner class CoursesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesViewHolder {
        _binding = CourseItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return CoursesViewHolder(_binding.root)
    }

    override fun onBindViewHolder(holder: CoursesViewHolder, position: Int) {
        val binding = CourseItemLayoutBinding.bind(holder.itemView)

        val course = courses[position]
        val date = course.dateCreated?.toDate()
        val formattedDate = date?.let {
            SimpleDateFormat.getDateInstance().format(date)
        }

        Log.d("Elezua", course.courseCode!!)

        binding.run {
            /*Picasso.get().load(course.thumbnail)
                .resize(2048, 2048)
                .placeholder(R.drawable.ic_round_image)
                .into(courseThumbnailView)*/

            Glide.with(context)
                .load(course.thumbnail)
                .placeholder(R.drawable.background)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.background)
                .fallback(R.drawable.background)
                .centerCrop()
                .into(courseThumbnailView)

            courseCodeView.text = course.courseCode
            courseTitleView.text = course.courseTitle
            schoolView.text = course.school
            dateView.text = formattedDate
        }

        binding.root.setOnClickListener {
            onCourseItemClick.onClick(courses, position)
        }
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    fun update(newCourses: List<Course>) {
        val diffCallback = MyCoursesDiffCallBack(courses, newCourses)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        courses.clear()
        courses.addAll(newCourses)
        diffResult.dispatchUpdatesTo(this)
    }

    class MyCoursesDiffCallBack(
        private val oldList: List<Course>,
        private val newList: List<Course>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }

    interface OnCourseItemClick {
        fun onClick(courseList: List<Course>, position: Int)
    }

}