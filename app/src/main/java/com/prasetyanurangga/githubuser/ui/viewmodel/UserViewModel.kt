package com.prasetyanurangga.githubuser.ui.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.prasetyanurangga.githubuser.data.model.UserModel
import com.prasetyanurangga.githubuser.data.repository.UserRepository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers

class UserViewModel (private var userRepository: UserRepository): ViewModel(){

    fun getUser(q: String,page: Int, perPage: Int): LiveData<List<UserModel>> = liveData(Dispatchers.IO){
        emit(userRepository.getUsers(q, page, perPage));
    }


}