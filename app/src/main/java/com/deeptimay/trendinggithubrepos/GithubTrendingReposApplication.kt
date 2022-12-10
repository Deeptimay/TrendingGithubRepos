package com.deeptimay.trendinggithubrepos

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

open class GithubTrendingReposApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}