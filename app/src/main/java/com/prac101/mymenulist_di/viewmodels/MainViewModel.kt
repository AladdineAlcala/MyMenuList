package com.prac101.mymenulist_di.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prac101.mymenulist_di.repository.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class StartDestination {
    SPLASH,    // The initial loading/checking screen
    HOME,      // The main content screen
    LOGIN      // The login screen
}
@HiltViewModel
class MainViewModel @Inject constructor (
    private val authManager: AuthManager) : ViewModel()
{
    private val _startDestination = MutableStateFlow(StartDestination.SPLASH)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            // This coroutineScope will complete only after all its children (async tasks) are done.
            coroutineScope {
                val authCheckDeferred = async {
                    authManager.getAccessToken() != null
                }

                // Do the same for the second call.
                val minDelayDeferred = async {
                    delay(2000L)
                }
                // --- END OF FIX ---

                // Now, wait for both tasks to complete
                val isLoggedIn = authCheckDeferred.await()
                minDelayDeferred.await()

                // After both are done, set the final destination
                _startDestination.value = if (isLoggedIn) {
                    StartDestination.HOME
                } else {
                    StartDestination.LOGIN
                }
            }
        }
    }
}