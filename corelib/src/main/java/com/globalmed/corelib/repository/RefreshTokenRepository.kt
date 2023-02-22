package com.globalmed.corelib.repository

import com.globalmed.corelib.request.RefreshTokenRequest
import com.globalmed.corelib.response.RefreshTokenResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenRepository {
    @POST("auth/refresh-tokens")
    fun getAuthToken(@Body request: RefreshTokenRequest): Call<RefreshTokenResponse>
}