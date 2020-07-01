package com.prasetyanurangga.githubuser.data.di.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prasetyanurangga.githubuser.data.repository.UserRepository
import com.prasetyanurangga.githubuser.ui.viewmodel.UserViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserViewModelFactory @Inject constructor(private var userRepository: UserRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(userRepository) as T
    }
}