package com.prasetyanurangga.githubuser.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.prasetyanurangga.githubuser.BR
import com.prasetyanurangga.githubuser.R
import com.prasetyanurangga.githubuser.data.model.UserModel
import com.prasetyanurangga.githubuser.databinding.ListItemUserBinding

class UserAdapter(private var userList: List<UserModel>?): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        return UserViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item_user, parent, false))
    }

    fun updateUser(newUser: List<UserModel>?)
    {
        userList = newUser
    }

    fun addUser(newUser: List<UserModel>?)
    {
        userList = merge(userList, newUser)
        Log.e("semua", userList.toString())
    }

    fun <UserModel> merge(first: List<UserModel>?, second: List<UserModel>?): List<UserModel> {
        val list: MutableList<UserModel> = ArrayList()
        list.addAll(first!!)
        list.addAll(second!!)
        return list
    }

    override fun getItemCount(): Int {
        return userList?.size ?: 0
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        holder.dataBinding.setVariable(BR.user, userList?.get(position))
    }

    class UserViewHolder(binding: ListItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        var dataBinding = binding
    }

}


