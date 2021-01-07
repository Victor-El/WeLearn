package me.codeenzyme.welearn.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.codeenzyme.welearn.databinding.FragmentCourseContentBinding
import me.codeenzyme.welearn.view.adapters.CoursesRecyclerViewAdapter
import me.codeenzyme.welearn.viewmodel.CoursesViewModel

@AndroidEntryPoint
class CourseContentFragment : Fragment() {

    /*private val coursesViewModel by viewModels<CoursesViewModel>()

    private val layoutManager = LinearLayoutManager(context)

    private val adapter = CoursesRecyclerViewAdapter(requireContext())

    private lateinit var binding: FragmentCourseContentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCourseContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.courseContentRecyclerView.adapter = adapter
        binding.courseContentRecyclerView.layoutManager = layoutManager

        loadData()
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.shimmer.root.isVisible = true
            binding.mainConstrainedContent.isVisible = false

            val courses = coursesViewModel.getCourses()
            adapter.update(courses)

            binding.shimmer.root.isVisible = false
            binding.mainConstrainedContent.isVisible = true
        }
    }*/

}