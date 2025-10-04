package com.prac101.mymenulist_di.viewmodels

import androidx.lifecycle.ViewModel
import com.prac101.mymenulist_di.repository.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {
    fun clearTokens() {
        authManager.clearTokens()
    }
}