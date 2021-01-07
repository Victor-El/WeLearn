package me.codeenzyme.welearn.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.codeenzyme.welearn.databinding.FragmentCourseContentBinding
import me.codeenzyme.welearn.databinding.FragmentHomeBinding
import me.codeenzyme.welearn.model.Course
import me.codeenzyme.welearn.view.adapters.CoursesRecyclerViewAdapter
import me.codeenzyme.welearn.viewmodel.CoursesViewModel

@AndroidEntryPoint
class HomeFragment : Fragment(), CoursesRecyclerViewAdapter.OnCourseItemClick {

    private val coursesViewModel by viewModels<CoursesViewModel>()

    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var adapter: CoursesRecyclerViewAdapter

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(requireContext())
        adapter = CoursesRecyclerViewAdapter(requireContext(), this)

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
    }

    override fun onClick(courseList: List<Course>, position: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToCourseTopicsFragment(courseList[position])
        findNavController().navigate(action)
    }
}