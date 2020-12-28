package me.codeenzyme.welearn.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.codeenzyme.welearn.model.User
import me.codeenzyme.welearn.repository.UserProfileRepository

class UserProfileViewModel @ViewModelInject constructor(
    private val userProfileRepository: UserProfileRepository
): ViewModel() {

    suspend fun getUserProfile() = userProfileRepository.getUserProfile()

    suspend fun updateUserProfile(photoUri: Uri?, user: User) =
        userProfileRepository.updateUserProfile(photoUri, user)

}