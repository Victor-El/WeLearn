package me.codeenzyme.welearn.repository

import android.net.Uri
import me.codeenzyme.welearn.data.remote.UserProfileService
import me.codeenzyme.welearn.model.User
import javax.inject.Inject

class UserProfileRepository @Inject constructor(
    private val userProfileService: UserProfileService
) {

    suspend fun getUserProfile() = userProfileService.getUserProfile()

    suspend fun updateUserProfile(photoUri: Uri?, user: User) =
        userProfileService.updateUserProfile(photoUri, user)
}