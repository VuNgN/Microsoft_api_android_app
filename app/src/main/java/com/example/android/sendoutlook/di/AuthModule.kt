package com.example.android.sendoutlook.di

import com.microsoft.graph.models.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Singleton
    @Provides
    fun isLoggedIn(): MutableStateFlow<Boolean> = MutableStateFlow(false)

    @Singleton
    @Provides
    fun getUser(): MutableStateFlow<User> = MutableStateFlow(User())
}