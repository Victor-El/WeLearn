package me.codeenzyme.welearn.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import me.codeenzyme.welearn.data.remote.AuthService
import me.codeenzyme.welearn.model.LoginResponse
import me.codeenzyme.welearn.model.RegisterResponse
import me.codeenzyme.welearn.model.User
import javax.inject.Inject

class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authService: AuthService
) {

    suspend fun registerUser(
        email: String,
        password: String,
        user: User,
    ): RegisterResponse = authService.registerUser(context, email, password, user)

    suspend fun loginUser(
        email: String,
        password: String,
    ): LoginResponse = authService.loginUser(context, email, password)

    fun logout() = authService.logout()

}