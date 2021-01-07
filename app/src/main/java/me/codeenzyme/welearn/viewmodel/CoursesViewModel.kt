package me.codeenzyme.welearn.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import me.codeenzyme.welearn.repository.CoursesRepository

class CoursesViewModel @ViewModelInject constructor(
    private val coursesRepository: CoursesRepository
) : ViewModel() {

    suspend fun getCourses() = coursesRepository.getCourses()

}