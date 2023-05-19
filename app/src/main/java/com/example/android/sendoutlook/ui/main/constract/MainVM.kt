package com.example.android.sendoutlook.ui.main.constract

import com.microsoft.graph.models.User
import kotlinx.coroutines.flow.MutableStateFlow

interface MainVM {
    val user: MutableStateFlow<User>
    suspend fun signOut()
}