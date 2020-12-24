package me.codeenzyme.welearn.model

sealed class RegisterResponse {
    data class Success(val message: String): RegisterResponse()
    data class Failure(val message: String): RegisterResponse()
}