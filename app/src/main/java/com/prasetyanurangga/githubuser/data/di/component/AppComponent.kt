package com.prasetyanurangga.githubuser.data.di.component

import android.app.Application
import com.prasetyanurangga.githubuser.ui.view.MainActivity
import com.prasetyanurangga.githubuser.data.di.module.AppModule
import com.prasetyanurangga.githubuser.data.di.module.NetworkModule
import com.prasetyanurangga.githubuser.data.repository.UserRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        AppModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(userRepository: UserRepository)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder


        @BindsInstance
        fun networkModule(networkModule: NetworkModule): Builder


        fun build(): AppComponent
    }
}