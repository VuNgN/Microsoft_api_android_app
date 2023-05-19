package com.example.android.sendoutlook.ui.outbox.contract.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.android.sendoutlook.ui.outbox.contract.OutboxVM
import com.example.android.sendoutlook.util.GraphHelper
import com.example.android.sendoutlook.util.SendingStatus
import com.microsoft.graph.models.BodyType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class OutboxVMImpl @Inject constructor(private val graphHelper: GraphHelper) : ViewModel(),
    OutboxVM {
    private var _sendingStatus: MutableStateFlow<SendingStatus> =
        MutableStateFlow(SendingStatus.FREE)
    override val sendingStatus: MutableStateFlow<SendingStatus>
        get() = _sendingStatus

    override fun sendMail(
        mailTo: String, cc: String, subject: String, bodyType: BodyType, content: String
    ) {
        _sendingStatus.value = SendingStatus.SENDING
        graphHelper.sendEmail(mailTo, cc, subject, bodyType, content).thenAccept {
            _sendingStatus.value = SendingStatus.SENT
        }.exceptionally { e ->
            _sendingStatus.value = SendingStatus.ERROR
            Log.e(TAG, "SEND EMAIL error: ${e.message}")
            null
        }
    }

    companion object {
        private val TAG = OutboxVMImpl::class.simpleName
    }
}