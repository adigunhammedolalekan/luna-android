package com.luna.chat.lunachat

import android.app.Application
import com.luna.chat.luna.Config
import com.luna.chat.luna.Luna

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val config = Config.Builder()
                .setURL("ws://192.168.43.39:8009/ws/connect")
                .addHeader("Authorization", "Kick-Ass-Auth-Key")
                .addHeader("X-Client-Type", "Android")
                .build()

        Luna.create(config) // connected to realtime world!
    }
}