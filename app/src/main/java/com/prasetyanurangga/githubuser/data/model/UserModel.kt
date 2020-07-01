package com.prasetyanurangga.githubuser.data.model

import androidx.lifecycle.LiveData
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("total_count")
    val itemCount: Int,
    @SerializedName("items")
    val items: List<UserModel>
)

data class UserModel(
    @SerializedName("avatar_url")
    val avatar: String,
    @SerializedName("login")
    val name: String,
    @SerializedName("url")
    val url: String
)