package me.codeenzyme.welearn.model

sealed class CoursesResponse {
    object Failure: CoursesResponse()
    data class Success(val courses: List<Course>): CoursesResponse()
}