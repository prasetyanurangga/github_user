package com.prasetyanurangga.githubuser.data.service

import com.prasetyanurangga.githubuser.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun getUsers(@Query("q") q: String, @Query("page") page: Int, @Query("per_page") perPage: Int): UserResponse
}