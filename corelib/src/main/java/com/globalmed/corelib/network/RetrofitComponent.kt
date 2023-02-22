package com.globalmed.corelib.network

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@EntryPoint
@Component(modules = arrayOf(RetrofitModule::class))
@InstallIn(SingletonComponent::class)
interface RetrofitComponent {
	@Component.Builder
	interface Builder{
		fun build(): RetrofitComponent

		fun application(@BindsInstance application: Application): Builder
	}

	fun retrofit(): Retrofit
}