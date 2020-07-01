package com.prasetyanurangga.githubuser.data.di.module

import com.prasetyanurangga.githubuser.data.repository.UserRepository
import com.prasetyanurangga.githubuser.data.service.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideUserRepository(apiService: ApiService): UserRepository{
        return UserRepository(apiService)
    }
}