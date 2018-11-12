package com.luna.chat.luna

import com.google.gson.Gson

class Channel(path: String) {

    var mPath: String = path
    var mListener: Listener? = null

    init {
        subscribe()
    }

    interface Listener {
        fun onMessage(data: String?)
    }

    public fun listener(listener: Listener) {
        mListener = listener
    }

    private fun subscribe() {

        val message = WsMessage<Any>("subscribe", mPath, Any());
        val json = Gson().toJson(message)

        Luna.instance?.registerObserver(mPath, object: WsListener {
            override fun onMessage(data: String?) {
                mListener?.onMessage(data)
            }

            override fun onConnect() {}

            override fun onError(error: LunaError) {}

        })
        Luna.instance?.send(json)
    }

    public fun sendMessage(data: Any) {

        val message = WsMessage<Any>("message", mPath, data)
        val json = Gson().toJson(message)

        Luna.instance?.send(json)
    }

    public fun unSubscribe() {
        Luna.instance?.unRegisterObserver(mPath)
    }
}