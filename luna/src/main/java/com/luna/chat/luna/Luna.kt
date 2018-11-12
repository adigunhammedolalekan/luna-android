package com.luna.chat.luna

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class Luna private constructor(config: Config) {

    private var mWebsocket: WebSocket? = null

    //Keep tracks of Websocket connection state
    private var mConnectionState: WebsocketClient.ConnectionState = WebsocketClient.ConnectionState.UNCONNECTED
    private var okHttpClient: OkHttpClient = OkHttpClient.Builder().build()
    private var mConfig: Config? = config
    private var mObservers = HashMap<String, WsListener>()

    init {
        connect()
    }

    class Config {
        var mUri: String = ""

    }

    companion object {

        public var instance: Luna? = null
        fun create(config: Config) {
            if (instance == null) {
                instance = Luna(config)
            }
        }
    }

    private fun connect() {

        if (mConnectionState == WebsocketClient.ConnectionState.CONNECTING ||
                mConnectionState == WebsocketClient.ConnectionState.CONNECTED) return

        mConnectionState = WebsocketClient.ConnectionState.CONNECTING
        val url = mConfig?.mUri
        try {
            val request = Request.Builder().url(url).build()
            mWebsocket = okHttpClient.newWebSocket(request, WebsocketClient(object: WsListener {

                override fun onConnect() {

                    mConnectionState = WebsocketClient.ConnectionState.CONNECTED
                    mObservers.forEach {next ->
                        val subscribeMessage = WsMessage("subscribe", next.key, null)
                        val json = Gson().toJson(subscribeMessage)

                        send(json)
                    }
                }

                override fun onError(error: LunaError) {

                    mConnectionState = WebsocketClient.ConnectionState.ERRORED
                    mWebsocket = null
                    connect(); //reconnect immediately

                    //notify all observers of errors
                    mObservers.forEach {wsListener ->
                        wsListener.value.onError(error)
                    }
                }

                override fun onMessage(data: String?) {

                    mObservers.forEach { wsListener ->
                        wsListener.value.onMessage(data)
                    }
                }
            }))
        }catch (e: Exception) {
            L.fine(e.message!!)
        }
    }

    public fun registerObserver(key: String, listener: WsListener) {

        if (mObservers.get(key) == null) {
            mObservers.put(key, listener)
        }
    }

    public fun unRegisterObserver(key: String) {

        mObservers.remove(key)
    }

    public fun send(data: String) {

        try {
            mWebsocket?.send(data)
        }catch (e: Exception) {}
    }
}