package com.prac101.mymenulist_di.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class RegisterUserRequest (
    @Json(name = "email")
    val email: String,
    @Json(name = "userName")
    val userName: String,
    @Json(name = "password")
    val password: String
)
