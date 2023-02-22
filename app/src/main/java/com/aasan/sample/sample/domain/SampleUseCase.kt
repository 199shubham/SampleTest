package com.aasan.sample.sample.domain


import com.aasan.sample.sample.data.SampleData

import retrofit2.Response

interface SampleUseCase {
    suspend fun getSampleData(): Response<SampleData>
}