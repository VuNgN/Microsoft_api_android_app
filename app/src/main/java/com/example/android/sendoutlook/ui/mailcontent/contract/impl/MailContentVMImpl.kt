package com.example.android.sendoutlook.ui.mailcontent.contract.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.sendoutlook.ui.mailcontent.contract.MailContentVM
import com.example.android.sendoutlook.util.GraphHelper
import com.microsoft.graph.models.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MailContentVMImpl @Inject constructor(private val graphHelper: GraphHelper) : ViewModel(),
    MailContentVM {
    private val _message = MutableStateFlow(Message())
    private val _loading = MutableStateFlow(false)
    override val loading: MutableStateFlow<Boolean>
        get() = _loading
    override val message: MutableStateFlow<Message>
        get() = _message

    override suspend fun getMessage(id: String) {
        _loading.value = true
        graphHelper.getMessage(id).thenAccept { message ->
            viewModelScope.launch {
                _message.emit(message)
                _loading.emit(false)
            }
        }.exceptionally { e ->
            _loading.value = false
            null
        }
    }
}