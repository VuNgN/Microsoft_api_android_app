package com.example.android.sendoutlook.di

import com.example.android.sendoutlook.util.AuthenticationHelper
import com.example.android.sendoutlook.util.GraphHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HelperModule {
    @Singleton
    @Provides
    fun getAuthHelper(): AuthenticationHelper =
        AuthenticationHelper.getInstance()

    @Singleton
    @Provides
    fun getGraphHelper(): GraphHelper = GraphHelper.getInstance()
}