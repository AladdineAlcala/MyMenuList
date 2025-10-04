package com.prac101.mymenulist_di.dto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents the full authentication response from the API.
 * This class can model both success and error responses based on the `success` flag.
 */
@JsonClass(generateAdapter = true)
data class LoginResponse(
    // Common fields
    @Json(name = "success")
    val success: Boolean,

    @Json(name = "message")
    val message: String?,

    // --- Fields present on SUCCESS ---
    @Json(name = "accessToken")
    val accessToken: String?, // Nullable because it won't exist in an error response

    @Json(name = "accessTokenExpires")
    val accessTokenExpires: String?,

    @Json(name = "refreshTokenResponse")
    val refreshTokenResponse: RefreshTokenData?, // The nested object

    // --- Fields present on ERROR ---
    @Json(name = "errorTitle")
    val errorTitle: String?, // Nullable because it will be empty in a success response

    @Json(name = "errorMessage")
    val errorMessage: String?,

    @Json(name = "errorCode")
    val errorCode: String?
)

/**
 * Represents the nested `refreshTokenResponse` object within the main AuthResponse.
 */
@JsonClass(generateAdapter = true)
data class RefreshTokenData(
    @Json(name = "id")
    val id: Int,

    @Json(name = "token")
    val token: String,

    @Json(name = "userId")
    val userId: String,

    @Json(name = "expires")
    val expires: String,

    @Json(name = "created")
    val created: String,

    @Json(name = "revoked")
    val revoked: String?, // Nullable to handle empty strings

    @Json(name = "isRememberMe")
    val isRememberMe: String, // Note: The JSON provides "True" as a String, not a Boolean

    @Json(name = "deviceInfo")
    val deviceInfo: String?,

    @Json(name = "ipAddress")
    val ipAddress: String?
)