package me.codeenzyme.welearn.data.remote

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import me.codeenzyme.welearn.model.ProfileUpdateResponse
import me.codeenzyme.welearn.model.User
import me.codeenzyme.welearn.model.UserProfileResponse
import me.codeenzyme.welearn.utils.USERS_COLLECTION
import me.codeenzyme.welearn.utils.USERS_IMAGE_FOLDER
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class FirebaseUserProfileService @Inject constructor(
): UserProfileService {
    val firebaseAuth = Firebase.auth
    val firestore = Firebase.firestore
    val storage = Firebase.storage

    override suspend fun updateUserProfile(photoUri: Uri?, user: User): ProfileUpdateResponse {
        return try {
            firebaseAuth.currentUser?.updateProfile(
                userProfileChangeRequest {
                    if (photoUri != null) {
                        val uploadRef = storage.reference.child("$USERS_IMAGE_FOLDER/${UUID.randomUUID()}")
                        uploadRef.putFile(photoUri).await()
                        this.photoUri = uploadRef.downloadUrl.await()
                    }

                    displayName = "${user.firstName} ${user.lastName}"
                }
            )?.await()
            if (user.email != firebaseAuth.currentUser?.email) {
                firebaseAuth.currentUser?.updateEmail(user.email)?.await()
                firebaseAuth.currentUser?.sendEmailVerification()?.await()
            }
            firestore.collection(USERS_COLLECTION).document(firebaseAuth.currentUser?.uid!!).set(user).await()
            ProfileUpdateResponse.Success
        } catch (e: Exception) {
            ProfileUpdateResponse.Failure
        }
    }

    override suspend fun getUserProfile(): UserProfileResponse {
        return try {
            val userProfile = firestore.collection(USERS_COLLECTION).document(firebaseAuth.currentUser?.uid!!)
                .get().await().toObject<User>()!!
            val userDisplayName = firebaseAuth.currentUser?.displayName!!
            val userPhotoUri = firebaseAuth.currentUser?.photoUrl
            UserProfileResponse.Success(
                userPhotoUri,
                userDisplayName,
                userProfile
            )
        } catch (e: Exception) {
            Log.d("FirebaseUserService", e.localizedMessage!!)
            UserProfileResponse.Failure
        }
    }
}