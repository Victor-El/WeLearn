package me.codeenzyme.welearn.data.remote

import me.codeenzyme.welearn.model.Course
import me.codeenzyme.welearn.model.CoursesResponse

interface CoursesService {

    suspend fun getCourses(): CoursesResponse

}