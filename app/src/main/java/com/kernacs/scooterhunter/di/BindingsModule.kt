package com.kernacs.scooterhunter.di

import com.kernacs.scooterhunter.data.repository.Repository
import com.kernacs.scooterhunter.data.repository.ScooterRepository
import com.kernacs.scooterhunter.data.source.local.LocalDataSource
import com.kernacs.scooterhunter.data.source.local.ScootersLocalDataSource
import com.kernacs.scooterhunter.data.source.remote.RemoteDataSource
import com.kernacs.scooterhunter.data.source.remote.ScootersRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ActivityComponent::class, ViewModelComponent::class)
abstract class BindingsModule {

    @Binds
    abstract fun providesRemoteDataSource(
        weatherRemoteDataSourceImpl: ScootersRemoteDataSource
    ): RemoteDataSource

    @Binds
    abstract fun providesRepository(
        repositoryImpl: ScooterRepository
    ): Repository

    @Binds
    abstract fun providesLocalDataSource(
        repositoryImpl: ScootersLocalDataSource
    ): LocalDataSource
}