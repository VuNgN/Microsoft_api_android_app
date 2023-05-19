package com.example.android.sendoutlook.ui.main.constract.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.android.sendoutlook.ui.main.constract.MainVM
import com.example.android.sendoutlook.util.AuthenticationHelper
import com.microsoft.graph.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainVMImpl @Inject constructor(
    private val _user: MutableStateFlow<User>,
    private val isLoggedIn: MutableStateFlow<Boolean>,
    private val authHelper: AuthenticationHelper
) : ViewModel(), MainVM {
    override val user: MutableStateFlow<User>
        get() = _user

    override suspend fun signOut() {
        authHelper.signOut()
        isLoggedIn.emit(false)
        Log.d(TAG, "SIGN OUT Success!")
    }

    companion object {
        private val TAG = MainVMImpl::class.simpleName
    }
}