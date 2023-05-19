package com.example.android.sendoutlook.ui.mailcontent.contract

import com.microsoft.graph.models.Message
import kotlinx.coroutines.flow.MutableStateFlow

interface MailContentVM {
    val loading: MutableStateFlow<Boolean>
    val message: MutableStateFlow<Message>
    suspend fun getMessage(id: String)
}