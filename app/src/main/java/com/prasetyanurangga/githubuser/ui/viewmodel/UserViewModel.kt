package com.prasetyanurangga.githubuser.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.prasetyanurangga.githubuser.data.repository.UserRepository
import com.prasetyanurangga.githubuser.util.Resource
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException

class UserViewModel (private var userRepository: UserRepository): ViewModel(){


    fun getUser(q: String,page: Int, perPage: Int) = liveData(Dispatchers.IO){
        try {
            emit(
                Resource.success( data = userRepository.getUsers(q, page, perPage))
            );
        }
        catch (exception :Exception)
        {
            val message = if(exception is HttpException) "Respon Server : "+(exception as HttpException).message() else exception.toString()
            emit(
                Resource.error(data = null, message = message)
            )
        }
    }


}