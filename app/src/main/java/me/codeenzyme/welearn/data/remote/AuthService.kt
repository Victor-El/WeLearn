package me.codeenzyme.welearn.data.remote

import android.content.Context
import me.codeenzyme.welearn.model.LoginResponse
import me.codeenzyme.welearn.model.RegisterResponse
import me.codeenzyme.welearn.model.User

interface AuthService {

    suspend fun registerUser(context: Context, email: String, password: String, user: User): RegisterResponse
    suspend fun loginUser(context: Context, email: String, password: String): LoginResponse
    fun logout()

}