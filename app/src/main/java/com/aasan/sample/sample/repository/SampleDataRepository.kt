package com.aasan.sample.sample.repository

import com.aasan.sample.sample.data.SampleData
import com.aasan.sample.common.ApiEndPoints

import retrofit2.Response
import retrofit2.http.*

interface SampleDataRepository {
    @GET(ApiEndPoints.SAMPLE_DATA)
    suspend fun getSampleData(@Query("title") title: String): Response<SampleData>

}