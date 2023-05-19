package com.example.android.sendoutlook.ui.inbox.contract.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.sendoutlook.ui.inbox.contract.InboxVM
import com.example.android.sendoutlook.util.GraphHelper
import com.google.gson.Gson
import com.microsoft.graph.models.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InboxVMImpl @Inject constructor(private val graphHelper: GraphHelper) : ViewModel(), InboxVM {
    private val _messages: MutableStateFlow<List<Message>> = MutableStateFlow(listOf())
    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val messages: MutableStateFlow<List<Message>>
        get() = _messages
    override val loading: MutableStateFlow<Boolean>
        get() = _loading

    override

    fun getMail() {
        _loading.value = true
        graphHelper.emails.thenAccept { mails ->
            Log.d(TAG, "getMail: ${Gson().toJson(mails.currentPage)}")
            mails.currentPage.forEach { mail ->
                Log.d(TAG, "Mail title: ${mail.subject}")
                Log.d(TAG, "Mail body: ${mail.body}")
            }
            viewModelScope.launch {
                _messages.emit(mails.currentPage)
                _loading.emit(false)
            }
        }
    }

    companion object {
        private val TAG = InboxVMImpl::class.simpleName
    }
}