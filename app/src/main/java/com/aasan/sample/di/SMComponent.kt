package com.aasan.sample.di

import android.app.Application

import com.globalmed.corelib.di.RepositoryComponent
import com.globalmed.corelib.network.RetrofitComponent
import dagger.BindsInstance
import dagger.Component
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@Component(dependencies = arrayOf(RetrofitComponent::class, RepositoryComponent::class),
    modules = arrayOf(SMModule::class))
@InstallIn(SingletonComponent::class)
interface SMComponent {
    @Component.Builder
    interface Builder{
        fun build(): SMComponent
        fun buildRetrofit(retrofitComponent: RetrofitComponent): Builder
        fun buildRepository(repositoryComponent: RepositoryComponent): Builder

        fun application(@BindsInstance application: Application): Builder
    }

}