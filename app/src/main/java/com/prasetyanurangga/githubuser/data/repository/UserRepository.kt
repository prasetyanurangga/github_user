package com.prasetyanurangga.githubuser.data.repository

import androidx.lifecycle.LiveData
import com.prasetyanurangga.githubuser.data.model.UserModel
import com.prasetyanurangga.githubuser.data.service.ApiService
import javax.inject.Inject

class UserRepository(private val apiService: ApiService) {


    suspend fun getUsers(q: String, page: Int, perPage: Int): List<UserModel> {
        return apiService.getUsers(q, page, perPage).items
    }
}