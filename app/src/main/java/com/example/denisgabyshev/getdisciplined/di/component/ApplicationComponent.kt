package com.example.denisgabyshev.getdisciplined.di.component

import android.content.Context
import com.example.denisgabyshev.getdisciplined.App
import com.example.denisgabyshev.getdisciplined.di.ApplicationContext
import com.example.denisgabyshev.getdisciplined.di.module.ApplicationModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by denisgabyshev on 10/09/2017.
 */
@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(app: App)

//    @ApplicationContext
//    fun context(): Context
}