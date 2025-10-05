package com.prac101.mymenulist_di.remote

import com.prac101.mymenulist_di.dto.LoginRequest
import com.prac101.mymenulist_di.dto.LoginResponse
import com.prac101.mymenulist_di.dto.RefreshTokenRequest
import com.prac101.mymenulist_di.dto.RegisterUserRequest
import com.prac101.mymenulist_di.dto.RegisterUserResponse
import com.prac101.mymenulist_di.dto.TokenResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

public interface ApiService{

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("user/register")
    suspend fun register(@Body registerUserRequest: RegisterUserRequest): Response<RegisterUserResponse>

    @POST("auth/refresh")
    fun refreshToken(@Body request: RefreshTokenRequest): Call<TokenResponse>
}