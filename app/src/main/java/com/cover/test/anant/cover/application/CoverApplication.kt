package com.cover.test.anant.cover.application

import android.app.Application
import android.content.Context
import io.realm.BuildConfig
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by anant on 2018-12-21.
 */
class CoverApplication : Application() {
    companion object {
        var instance: CoverApplication? = null
            private set

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val configBuilder = RealmConfiguration.Builder()
                .name("cover.realm")
                .build()

        Realm.setDefaultConfiguration(configBuilder)

        if (BuildConfig.BUILD_TYPE == "debug") {
            configBuilder.shouldDeleteRealmIfMigrationNeeded()
        }
    }
}