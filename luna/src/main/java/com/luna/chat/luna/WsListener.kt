package com.luna.chat.luna

public interface WsListener {

    fun onConnect()

    fun onMessage(data: String?)

    fun onError(error: LunaError)
}