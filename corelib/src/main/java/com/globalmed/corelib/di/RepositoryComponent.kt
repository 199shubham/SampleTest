package com.globalmed.corelib.di

import android.app.Application
import com.globalmed.corelib.repository.SharedPreferenceRepository
import dagger.BindsInstance
import dagger.Component
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@EntryPoint
@Component(modules = arrayOf(RepositoryModule::class))
@InstallIn(SingletonComponent::class)
interface RepositoryComponent {
	@Component.Builder
	interface Builder{
		fun build(): RepositoryComponent


		fun application(@BindsInstance application: Application): Builder
	}

	fun sharedPreferenceRepository(): SharedPreferenceRepository
}