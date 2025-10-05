package com.prac101.mymenulist_di.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prac101.mymenulist_di.dto.RegisterUserResponse
import com.prac101.mymenulist_di.repository.RegisterRepository
import com.prac101.mymenulist_di.common.Result
import com.prac101.mymenulist_di.dto.RegisterUserRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {
    private val _registerState = MutableStateFlow<Result<RegisterUserResponse>>(Result.Idle)
    val registerState = _registerState.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun register(userName: String, email: String, password: String) {
        // Prevent multiple simultaneous register attempts
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _registerState.value = Result.Idle // Reset state before new request
            val request = RegisterUserRequest(userName = userName, email = email, password = password)
            val result = registerRepository.register(request)
            _registerState.value = result
            _isLoading.value = false
        }
    }
}