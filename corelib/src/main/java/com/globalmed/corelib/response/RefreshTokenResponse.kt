package com.globalmed.corelib.response

data class RefreshTokenResponse(
    val access: Access,
    val refresh: Refresh
)