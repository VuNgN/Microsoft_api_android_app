package com.example.android.sendoutlook.ui.outbox.contract

import com.example.android.sendoutlook.util.SendingStatus
import com.microsoft.graph.models.BodyType
import kotlinx.coroutines.flow.MutableStateFlow

interface OutboxVM {
    val sendingStatus: MutableStateFlow<SendingStatus>
    fun sendMail(mailTo: String, cc: String, subject: String, bodyType: BodyType, content: String)
}