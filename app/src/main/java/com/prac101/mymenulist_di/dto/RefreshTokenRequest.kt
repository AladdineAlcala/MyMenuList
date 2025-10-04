package com.prac101.mymenulist_di.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class RefreshTokenRequest(
    @Json(name = "refreshToken")
    val refreshToken: String
)