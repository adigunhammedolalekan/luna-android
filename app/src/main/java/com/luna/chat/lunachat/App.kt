package com.luna.chat.lunachat

import android.app.Application
import com.luna.chat.luna.Luna

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val config = Luna.Config()
        config.mUri = "ws://192.168.43.39:8009/ws/connect"
        Luna.create(config)
    }
}