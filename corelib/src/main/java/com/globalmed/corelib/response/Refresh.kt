package com.globalmed.corelib.response

data class Refresh(
    val expires: String,
    val token: String
)