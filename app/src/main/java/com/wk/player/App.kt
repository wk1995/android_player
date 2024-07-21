package com.wk.player

import android.app.Application

/**
 *
 *      author : wk
 *      e-mail : 1226426603@qq.com
 *      time   : 2024/7/21
 *      desc   :
 *      GitHub : https://github.com/wk1995
 *      CSDN   : http://blog.csdn.net/qq_33882671
 * */
class App : Application() {


    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}