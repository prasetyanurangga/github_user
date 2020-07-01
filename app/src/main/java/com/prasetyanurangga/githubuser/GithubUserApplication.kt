package com.prasetyanurangga.githubuser

import android.app.Application
import com.prasetyanurangga.githubuser.data.di.component.AppComponent
import com.prasetyanurangga.githubuser.data.di.component.DaggerAppComponent
import com.prasetyanurangga.githubuser.data.di.module.AppModule
import com.prasetyanurangga.githubuser.data.di.module.NetworkModule

class GithubUserApplication: Application() {

    companion object {
        lateinit var instance: GithubUserApplication
    }

    lateinit var appComponent: AppComponent

    override fun onCreate(){
        super.onCreate()
        instance = this

        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .networkModule(NetworkModule())
            .build()

    }
}