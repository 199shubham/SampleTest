package com.aasan.sample.sample.domain


import com.aasan.sample.sample.data.SampleData
import com.aasan.sample.sample.repository.SampleDataRepository

import com.globalmed.corelib.repository.SharedPreferenceRepository
import retrofit2.Response

class SampleUseCaseImpl(private val sharedPreferenceRepository: SharedPreferenceRepository,
                        private val repository: SampleDataRepository): SampleUseCase {


    override suspend fun getSampleData(): Response<SampleData>{
        return repository.getSampleData("cat")
    }
}