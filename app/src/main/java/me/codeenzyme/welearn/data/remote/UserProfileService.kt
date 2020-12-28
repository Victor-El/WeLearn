package me.codeenzyme.welearn.data.remote

import android.net.Uri
import me.codeenzyme.welearn.model.ProfileUpdateResponse
import me.codeenzyme.welearn.model.User
import me.codeenzyme.welearn.model.UserProfileResponse

interface UserProfileService {

    suspend fun updateUserProfile(photoUri: Uri?, user: User): ProfileUpdateResponse

    suspend fun getUserProfile(): UserProfileResponse

}