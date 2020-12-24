package me.codeenzyme.welearn.data.remote

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import me.codeenzyme.welearn.R
import me.codeenzyme.welearn.utils.USERS
import me.codeenzyme.welearn.model.LoginResponse
import me.codeenzyme.welearn.model.RegisterResponse
import me.codeenzyme.welearn.model.User
import java.lang.Exception
import javax.inject.Inject

class FirebaseAuthService @Inject constructor() : AuthService {
    val firebaseAuth = Firebase.auth
    val firestore = Firebase.firestore

    override suspend fun registerUser(context: Context, email: String, password: String, user: User): RegisterResponse {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            firebaseAuth.currentUser?.updateProfile(userProfileChangeRequest {
                displayName = "${user.firstName} ${user.lastName}"
            })?.await()
            firestore.collection(USERS).document(authResult.user!!.uid).set(user).await()
            firebaseAuth.currentUser?.sendEmailVerification()?.await()
            RegisterResponse.Success(context.getString(R.string.register_success))
        } catch (e: Exception) {
            RegisterResponse.Failure(e.localizedMessage!!)
        }
    }

    override suspend fun loginUser(
        context: Context,
        email: String,
        password: String
    ): LoginResponse {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            LoginResponse.Success(context.getString(R.string.login_success))
        } catch (e: Exception) {
            LoginResponse.Failure(e.localizedMessage!!)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }


}