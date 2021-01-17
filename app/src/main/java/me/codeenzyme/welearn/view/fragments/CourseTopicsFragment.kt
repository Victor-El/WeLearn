package me.codeenzyme.welearn.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import me.codeenzyme.welearn.R
import me.codeenzyme.welearn.databinding.FragmentCourseTopicsBinding
import me.codeenzyme.welearn.model.Course
import me.codeenzyme.welearn.view.adapters.TopicsRecyclerViewAdapter

@AndroidEntryPoint
class CourseTopicsFragment : Fragment() {

    private val courseArgs by navArgs<CourseTopicsFragmentArgs>()

    private lateinit var _binding: FragmentCourseTopicsBinding

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var topicsRecyclerViewAdapter: TopicsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCourseTopicsBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCourseTopicsBinding.bind(view)

        // set up navigation with toolbar
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.toolbar.setupWithNavController(findNavController(), appBarConfiguration)

        customizeAppBarLayout(binding)

        setUpRecyclerView(binding)

    }

    private fun customizeAppBarLayout(binding: FragmentCourseTopicsBinding) {
        Glide.with(requireContext())
            .load(courseArgs.course.thumbnail)
            .placeholder(R.drawable.background)
            .error(R.drawable.background)
            .fallback(R.drawable.background)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.bannerImage)

        binding.toolbar.title = courseArgs.course.courseCode
        binding.toolbar.subtitle = courseArgs.course.courseTitle
        // binding.toolbar.setTitleTextColor(Color.WHITE)
    }

    private fun setUpRecyclerView(binding: FragmentCourseTopicsBinding) {
        topicsRecyclerViewAdapter = TopicsRecyclerViewAdapter(
            requireContext(),
            object : TopicsRecyclerViewAdapter.OnTopicsClickListener {
                override fun onClick(topics: List<Course.Topic>, position: Int) {
                    Snackbar.make(binding.topicsRecyclerView, "Click", Snackbar.LENGTH_LONG).show()
                }

                override fun onClickDownload(topics: List<Course.Topic>, position: Int) {
                    Snackbar.make(binding.root, "Download", Snackbar.LENGTH_LONG).show()
                }

            })
        layoutManager = LinearLayoutManager(requireContext())

        binding.topicsRecyclerView.layoutManager = layoutManager
        binding.topicsRecyclerView.adapter = topicsRecyclerViewAdapter
        topicsRecyclerViewAdapter.update(courseArgs.course.topics!!)
    }

}