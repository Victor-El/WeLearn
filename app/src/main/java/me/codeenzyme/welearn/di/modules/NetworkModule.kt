package me.codeenzyme.welearn.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import me.codeenzyme.welearn.data.remote.AuthService
import me.codeenzyme.welearn.data.remote.FirebaseAuthService
import me.codeenzyme.welearn.data.remote.FirebaseUserProfileService
import me.codeenzyme.welearn.data.remote.UserProfileService
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
abstract class NetworkModule {

    @Singleton
    @Binds
    abstract fun bindAuthService(firebaseAuthService: FirebaseAuthService): AuthService

    @Singleton
    @Binds
    abstract fun bindUserProfileService(firebaseUserProfileService: FirebaseUserProfileService): UserProfileService

}