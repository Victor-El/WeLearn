package me.codeenzyme.welearn.data.remote

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import me.codeenzyme.welearn.model.Course
import me.codeenzyme.welearn.utils.COURSES_COLLECTION
import javax.inject.Inject

class FirebaseCoursesService @Inject constructor() : CoursesService {

    val firebaseAuth = Firebase.auth
    val firestore = Firebase.firestore

    override suspend fun getCourses(): List<Course> {
        return firestore.collection(COURSES_COLLECTION)
            .get().await().toObjects<Course>()
    }
}