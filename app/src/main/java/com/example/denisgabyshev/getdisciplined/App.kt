package com.example.denisgabyshev.getdisciplined

import android.app.Application
import com.example.denisgabyshev.getdisciplined.di.component.ApplicationComponent
import com.example.denisgabyshev.getdisciplined.di.component.DaggerApplicationComponent
import com.example.denisgabyshev.getdisciplined.di.module.ApplicationModule

/**
 * Created by denisgabyshev on 10/09/2017.
 */
class App : Application() {

     private val applicationComponent: ApplicationComponent by lazy {
         DaggerApplicationComponent
                 .builder()
                 .applicationModule(ApplicationModule(this))
                 .build()
     }

    override fun onCreate() {
        super.onCreate()
        applicationComponent.inject(this)
    }

    fun component() = applicationComponent
}