package com.prac101.mymenulist_di.repository

import com.prac101.mymenulist_di.dto.RegisterUserRequest
import com.prac101.mymenulist_di.dto.RegisterUserResponse
import com.prac101.mymenulist_di.remote.ApiService
import com.prac101.mymenulist_di.common.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepository @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager,
) {
   suspend fun register(registerUserRequest: RegisterUserRequest):Result<RegisterUserResponse> {
       return withContext(Dispatchers.IO) {
           try {
               val response = apiService.register(registerUserRequest)
               if (response.isSuccessful && response.body() != null) {
                   val registerUserResponse = response.body()!!
                   if(registerUserResponse.success == "True" && registerUserResponse.userId.isNotEmpty()){
                       Result.Success(registerUserResponse)
                   }
                   else{
                       Result.Error<RegisterUserResponse>(registerUserResponse.errorMessage)
                   }
               } else {
                   Result.Error("Server error: ${response.code()}")
               }
           }
           catch (e: Exception) {
               Result.Error("An unexpected error occurred: ${e.message}")
           }
       }
    }
}