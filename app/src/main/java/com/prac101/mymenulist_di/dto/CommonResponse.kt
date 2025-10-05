package com.prac101.mymenulist_di.dto

import com.squareup.moshi.Json

open class CommonResponse(
    @Json(name = "errorTitle")
    open val errorTitle: String = "",

    @Json(name = "errorMessage")
    open val errorMessage: String = "",

    @Json(name = "errorCode")
    open val  errorCode: String = ""
)