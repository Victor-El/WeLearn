package me.codeenzyme.welearn.model

sealed class ProfileUpdateResponse {
    object Failure: ProfileUpdateResponse()
    object Success: ProfileUpdateResponse()
}
