package com.globalmed.corelib.di

import android.app.Application
import com.globalmed.corelib.repository.SharedPreferenceRepository
import com.globalmed.corelib.repository.SharedPreferenceRepositoryImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideSharedPreferenceRepository(application: Application): SharedPreferenceRepository {
        return SharedPreferenceRepositoryImplementation(application)
    }
}