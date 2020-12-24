package me.codeenzyme.welearn.viewmodel

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import me.codeenzyme.welearn.model.LoginResponse
import me.codeenzyme.welearn.model.RegisterResponse
import me.codeenzyme.welearn.model.User
import me.codeenzyme.welearn.repository.AuthRepository

class AuthViewModel @ViewModelInject constructor(
    @ApplicationContext val context: Context,
    private val authRepository: AuthRepository,
): ViewModel() {

    suspend fun registerUser(email: String, password: String, user: User): RegisterResponse = authRepository.registerUser(email, password, user)
    suspend fun loginUser(email: String, password: String): LoginResponse = authRepository.loginUser(email, password)
    fun logout() = authRepository.logout()
}