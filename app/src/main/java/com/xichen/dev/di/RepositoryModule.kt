package com.xichen.dev.di

import com.xichen.dev.data.repository.AuthRepositoryImpl
import com.xichen.dev.data.repository.LanguageRepositoryImpl
import com.xichen.dev.data.repository.ScaffoldRepositoryImpl
import com.xichen.dev.domain.repository.AuthRepository
import com.xichen.dev.domain.repository.LanguageRepository
import com.xichen.dev.domain.repository.ScaffoldRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindScaffoldRepository(
        repositoryImpl: ScaffoldRepositoryImpl
    ): ScaffoldRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        repositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindLanguageRepository(
        repositoryImpl: LanguageRepositoryImpl
    ): LanguageRepository
}
