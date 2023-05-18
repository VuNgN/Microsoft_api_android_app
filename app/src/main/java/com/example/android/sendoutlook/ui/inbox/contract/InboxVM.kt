package com.example.android.sendoutlook.ui.inbox.contract

import com.microsoft.graph.models.Message
import kotlinx.coroutines.flow.MutableStateFlow

interface InboxVM {
    val messages: MutableStateFlow<List<Message>>
    val loading: MutableStateFlow<Boolean>
    fun getMail()
}