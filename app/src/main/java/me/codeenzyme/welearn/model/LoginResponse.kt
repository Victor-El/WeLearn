package me.codeenzyme.welearn.model

sealed class LoginResponse {
    data class Success(val message: String): LoginResponse()
    data class Failure(val message: String): LoginResponse()
}