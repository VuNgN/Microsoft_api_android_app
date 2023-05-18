package com.example.android.sendoutlook

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.android.sendoutlook.ui.login.Login
import com.example.android.sendoutlook.ui.main.AppContainer
import com.example.android.sendoutlook.ui.theme.SendOutlookTheme
import com.example.android.sendoutlook.util.AuthenticationHelper
import com.example.android.sendoutlook.util.GraphHelper
import com.google.gson.Gson
import com.microsoft.graph.models.User
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalUiRequiredException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var _authHelper: AuthenticationHelper
    private lateinit var _graphHelper: GraphHelper
    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    private val _isLoggedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            _isLoggedIn.value = savedInstanceState.getBoolean(SAVED_IS_SIGNED_IN)
        }
        AuthenticationHelper.getInstance(applicationContext).thenAccept { authHelper ->
            _authHelper = authHelper
            if (!_isLoggedIn.value) {
                _isLoading.value = true
                doSilentSignIn()
            } else {
                _isLoading.value = false
            }
        }
        setContent {
            val isLoggedIn by _isLoggedIn.collectAsState()
            val isLoading by _isLoading.collectAsState()
            val user by _user.collectAsState()
            val coroutineScope = rememberCoroutineScope()
            SendOutlookTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    if (isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        if (isLoggedIn) {
                            AppContainer(modifier = Modifier.fillMaxSize(),
                                username = user?.displayName,
                                email = user?.mail,
                                signOut = { coroutineScope.launch { signOut() } })
                        } else {
                            Login { coroutineScope.launch { signIn() } }
                        }
                    }
                }
            }
        }
    }

    private fun signIn() {
        _isLoading.value = true
        doInteractiveSignIn()
    }

    private fun signOut() {
        _isLoggedIn.value = false
        _authHelper.signOut()
    }

    private fun doSilentSignIn() {
        _authHelper.acquireTokenSilently().thenAccept(this::handleSignInSuccess)
            .exceptionally { exception ->
                val cause = exception.cause
                if (cause is MsalUiRequiredException || cause is MsalClientException) {
                    Log.e(TAG, "AUTH Interactive login required")
                    signOut()
                    doInteractiveSignIn()
                }
                Log.e(TAG, "AUTH error sign in: $exception")
                _isLoading.value = false
                null
            }
    }

    private fun doInteractiveSignIn() {
        _authHelper.acquireTokenInteractively(this).thenAccept(this::handleSignInSuccess)
            .exceptionally { exception ->
                Log.e(TAG, "AUTH error: $exception")
                _isLoading.value = false
                null
            }
    }

    private fun handleSignInSuccess(iAuthenticationResult: IAuthenticationResult) {
        _graphHelper = GraphHelper.getInstance()
        val accessToken = iAuthenticationResult.accessToken
        Log.d(TAG, "Access token: $accessToken")

        getUser()
    }

    private fun getUser() {
        _graphHelper.user.thenAccept { user ->
            _user.value = user
            Log.d(TAG, "User: ${Gson().toJson(user)}")
            lifecycleScope.launch(Dispatchers.IO) {
                _isLoggedIn.emit(true)
                _isLoading.emit(false)
            }
        }.exceptionally { exception ->
            Log.e(TAG, "AUTH error getting: $exception")
            _isLoading.value = false
            null
        }
    }

    companion object {
        private val TAG = MainActivity::class.simpleName
        private const val SAVED_IS_SIGNED_IN = "isSignedIn"
    }
}