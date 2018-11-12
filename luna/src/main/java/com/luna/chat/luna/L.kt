package com.luna.chat.luna

import android.util.Log

class L {

    companion object {

        fun fine(message: String) {
            Log.d("Luna", message)
        }
        fun error(e: LunaError) {
            Log.wtf("Luna", e.error)
        }
    }
}