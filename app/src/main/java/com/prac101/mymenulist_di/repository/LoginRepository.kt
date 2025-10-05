package com.prac101.mymenulist_di.repository

import com.prac101.mymenulist_di.dto.LoginRequest
import com.prac101.mymenulist_di.dto.LoginResponse
import com.prac101.mymenulist_di.remote.ApiService
import com.prac101.mymenulist_di.common.Result
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


// Using a sealed class for more structured state management


@Singleton
class LoginRepository @Inject constructor(
    private val apiService: ApiService,
    private val authManager: AuthManager,
    private val moshi: Moshi
) {
    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(loginRequest)
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    // If the login is successful, save the tokens
                    if (loginResponse.accessToken != null && loginResponse.refreshTokenResponse?.token != null) {
                        authManager.saveTokens(loginResponse.accessToken, loginResponse.refreshTokenResponse.token)
                        Result.Success(loginResponse)
                    } else {
                        // Handle cases where the API returns 200 OK but with an error message
                        Result.Error(loginResponse.errorMessage ?: "Successful response with no tokens.")
                    }
                } else {
                    // Handle unsuccessful HTTP responses (4xx, 5xx)
                    val statusCode = response.code()
                    // IMPORTANT: .string() consumes the error body, so call it only once.
                    val errorBodyString = response.errorBody()?.string()

                    val parsedError = parseErrorBody(errorBodyString)

                    val errorMessage = when (statusCode) {
                        401 -> "Unauthorized: Invalid email or password. ${parsedError.message}"
                        else -> "Server error ($statusCode): ${parsedError.message} (Code: ${parsedError.code})"
                    }
                    Result.Error(errorMessage.trim())
                }
            } catch (e: IOException) {
                Result.Error("Network error: Please check your connection.")
            } catch (e: Exception) {
                Result.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }

    /**
     * Helper function to parse the error JSON from the response body.
     */
    private fun parseErrorBody(errorBody: String?): ParsedError {
        if (errorBody.isNullOrEmpty()) {
            return ParsedError("Could not get error details from server.", "")
        }
        return try {
            val errorAdapter = moshi.adapter(LoginResponse::class.java)
            val errorResponse = errorAdapter.fromJson(errorBody)
            val message = errorResponse?.errorMessage?.takeIf { it.isNotBlank() } ?: "No error message provided."
            val code = errorResponse?.errorCode?.takeIf { it.isNotBlank() } ?: ""
            ParsedError(message, code)
        } catch (e: Exception) {
            ParsedError("Error parsing server response.", "")
        }
    }

    /**
     * A small helper data class to hold parsed error details.
     */
    private data class ParsedError(val message: String, val code: String)
}