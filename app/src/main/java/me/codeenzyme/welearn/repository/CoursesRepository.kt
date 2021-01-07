package me.codeenzyme.welearn.repository

import me.codeenzyme.welearn.data.remote.CoursesService
import javax.inject.Inject

class CoursesRepository @Inject constructor(
    private val coursesService: CoursesService
) {

    suspend fun getCourses() = coursesService.getCourses()

}