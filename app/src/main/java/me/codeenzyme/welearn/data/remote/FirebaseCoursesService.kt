package me.codeenzyme.welearn.data.remote

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import me.codeenzyme.welearn.model.Course
import me.codeenzyme.welearn.model.CoursesResponse
import me.codeenzyme.welearn.utils.COURSES_COLLECTION
import java.lang.Exception
import javax.inject.Inject

class FirebaseCoursesService @Inject constructor() : CoursesService {

    val firebaseAuth = Firebase.auth
    val firestore = Firebase.firestore

    override suspend fun getCourses(): CoursesResponse {
        return try {
            CoursesResponse.Success(firestore.collection(COURSES_COLLECTION)
                .get().await().toObjects<Course>())
        } catch (e: Exception) {
            Log.d(javaClass.name, e.localizedMessage!!)
            CoursesResponse.Failure
        }
    }
}