package com.luna.chat.luna

import com.google.gson.Gson

typealias ChannelListener = (String?) -> Unit


class Channel(path: String) {

    var mPath: String = path
    var channelListener: ChannelListener = {}

    init {
        subscribe()
    }

    private fun subscribe() {

        val message = WsMessage("subscribe", mPath, Any())
        val json = Gson().toJson(message)

        Luna.getInstance().registerObserver(mPath, object: WsListener {
            override fun onMessage(data: String?) {
                channelListener.invoke(data)
            }

            override fun onConnect() {}

            override fun onError(error: LunaError) {}

        })
        Luna.getInstance().send(json)
    }

    fun sendMessage(data: Any) {

        val message = WsMessage("message", mPath, data)
        val json = Gson().toJson(message)

        Luna.getInstance().send(json)
    }

    fun unSubscribe() {
        Luna.getInstance().unRegisterObserver(mPath)
    }
}