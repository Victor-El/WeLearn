package me.codeenzyme.welearn.data.remote

import me.codeenzyme.welearn.model.Course

interface CoursesService {

    suspend fun getCourses(): List<Course>

}