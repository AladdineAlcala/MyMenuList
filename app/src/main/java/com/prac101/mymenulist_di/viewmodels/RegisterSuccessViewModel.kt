package com.prac101.mymenulist_di.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterSuccessViewModel @Inject constructor(): ViewModel() {
    private val _isVerified = MutableStateFlow(false)
    val isVerified = _isVerified.asStateFlow()
    private var pollingJob: Job? = null

    /**
     * Starts a background polling process to check the email verification status.
     * It checks every 5 seconds.
     */
    fun startPollingForVerification(email: String) {

    }


    /**
     * Ensure we stop polling if the ViewModel is cleared (e.g., user navigates away).
     */
    override fun onCleared() {
        super.onCleared()
        pollingJob?.cancel()
    }
}