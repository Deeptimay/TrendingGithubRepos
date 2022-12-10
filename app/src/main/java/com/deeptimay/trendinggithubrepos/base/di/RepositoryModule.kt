package com.deeptimay.trendinggithubrepos.base.di

import com.deeptimay.trendinggithubrepos.repository.GithubRepository
import com.deeptimay.trendinggithubrepos.repository.GithubRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesGithubRepository(impl: GithubRepositoryImpl): GithubRepository

}