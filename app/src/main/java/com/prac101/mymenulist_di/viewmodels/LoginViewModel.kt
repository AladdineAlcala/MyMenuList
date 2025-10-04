package com.prac101.mymenulist_di.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prac101.mymenulist_di.dto.LoginRequest
import com.prac101.mymenulist_di.dto.LoginResponse
import com.prac101.mymenulist_di.repository.LoginRepository
import com.prac101.mymenulist_di.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<Result<LoginResponse>>(Result.Idle)
    val loginState = _loginState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun login(email: String, password: String, rememberMe: Boolean) {
        // Prevent multiple simultaneous login attempts
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _loginState.value = Result.Idle // Reset state before new request

            val request = LoginRequest(email = email, password = password, rememberMe = rememberMe)
            val result = loginRepository.login(request)

            _loginState.value = result
            _isLoading.value = false
        }
    }
}