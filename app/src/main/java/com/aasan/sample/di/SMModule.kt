package com.aasan.sample.di


import com.aasan.sample.sample.domain.SampleUseCase
import com.aasan.sample.sample.domain.SampleUseCaseImpl
import com.aasan.sample.sample.repository.SampleDataRepository
import com.globalmed.corelib.repository.SharedPreferenceRepository


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object SMModule {
    @Provides
    fun provideSampleRepository(retrofit: Retrofit) = retrofit.create(SampleDataRepository::class.java)


    @Provides
    fun provideSampleUseCase(repository: SampleDataRepository,
                             sharedPreferenceRepository: SharedPreferenceRepository
    ): SampleUseCase
            = SampleUseCaseImpl(sharedPreferenceRepository, repository)
}