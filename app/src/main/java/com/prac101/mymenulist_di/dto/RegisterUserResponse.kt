package com.prac101.mymenulist_di.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterUserResponse(
    @Json(name = "success")
    val success: String = "",
    @Json(name = "userId")
    val userId: String = "",
    override val errorCode: String="",
    override val errorMessage: String="",
    override val errorTitle: String=""
): CommonResponse()