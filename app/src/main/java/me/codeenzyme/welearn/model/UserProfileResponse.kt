package me.codeenzyme.welearn.model

import android.net.Uri

sealed class UserProfileResponse {
    object Failure: UserProfileResponse()
    data class Success(val photoUri: Uri?, val displayName: String,val user: User): UserProfileResponse()
}
