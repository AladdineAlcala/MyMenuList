package com.prac101.mymenulist_di.remote

import com.prac101.mymenulist_di.dto.LoginRequest
import com.prac101.mymenulist_di.dto.LoginResponse
import com.prac101.mymenulist_di.dto.RefreshTokenRequest
import com.prac101.mymenulist_di.dto.TokenResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

public interface ApiService{

    @POST("auth/login") // Replace with your actual login endpoint
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/refresh") // Replace with your actual token refresh endpoint
    fun refreshToken(@Body request: RefreshTokenRequest): Call<TokenResponse>
}