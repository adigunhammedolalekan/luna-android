package com.luna.chat.luna

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class Luna private constructor(config: Config) {

    private var mWebsocket: WebSocket? = null

    // Keep tracks of Websocket connection state
    private var mConnectionState: WebsocketClient.ConnectionState = WebsocketClient.ConnectionState.UNCONNECTED
    private var okHttpClient: OkHttpClient = OkHttpClient.Builder().build()
    private var mConfig: Config? = config
    private var mObservers = hashMapOf<String, WsListener>()

    init {
        connect()
    }

    companion object {

        private lateinit var INSTANCE: Luna
        fun create(config: Config) {

            if (config.getURL() == "") {
                throw IllegalArgumentException("URL should not be empty")
            }

            if (INSTANCE == null) {

                synchronized(this) {
                    INSTANCE = Luna(config)
                }
            }
        }

        fun getInstance() = INSTANCE
    }

    private fun connect() {

        if (mConnectionState == WebsocketClient.ConnectionState.CONNECTING ||
                mConnectionState == WebsocketClient.ConnectionState.CONNECTED) return

        mConnectionState = WebsocketClient.ConnectionState.CONNECTING
        val url = mConfig?.getURL()
        try {
            val builder = Request.Builder().url(url)
            val headers = mConfig?.getHeaders()

            // add request headers
            headers?.forEach { key, value ->
                builder.addHeader(key, value)
            }

            val request = builder.build()
            mWebsocket = okHttpClient.newWebSocket(request, WebsocketClient(object: WsListener {

                override fun onConnect() {

                    mConnectionState = WebsocketClient.ConnectionState.CONNECTED

                    // auto subscribe on connection established
                    mObservers.forEach {(key, value) ->
                        val subscribeMessage = WsMessage("subscribe", key, null)
                        val json = Gson().toJson(subscribeMessage)

                        send(json)
                    }
                }

                override fun onError(error: LunaError) {

                    mConnectionState = WebsocketClient.ConnectionState.ERRORED
                    mWebsocket = null
                    connect() // reconnect immediately

                    // notify all observers of errors
                    mObservers.forEach {(_, value) ->
                        value.onError(error)
                    }
                }

                override fun onMessage(data: String?) {

                    mObservers.forEach { wsListener ->
                        wsListener.value.onMessage(data)
                    }
                }
            }))
        }catch (e: Exception) {

        }
    }

    // add an observer
    fun registerObserver(key: String, listener: WsListener) {

        if (mObservers[key] == null) {
            mObservers[key] = listener
        }
    }

    // remove an observer
    fun unRegisterObserver(key: String) {
        mObservers.remove(key)
    }

    // send message to websocket server
    fun send(data: String): Boolean? {

        return try {
            mWebsocket?.send(data)
        }catch (e: Exception) {
            return false
        }
    }
}