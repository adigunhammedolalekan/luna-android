package com.luna.chat.luna

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebsocketClient(listener: WsListener) : WebSocketListener() {

    private val mListener: WsListener = listener

    public enum class ConnectionState {
        UNCONNECTED, CONNECTING, CONNECTED, ERRORED,
    }

    override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
        super.onClosed(webSocket, code, reason)

        val error = LunaError()
        error.code = code
        error.error = Throwable(reason)

        mListener.onError(error)
    }

    override fun onOpen(webSocket: WebSocket?, response: Response?) {
        super.onOpen(webSocket, response)

        mListener.onConnect()
        L.fine("Connected")
    }

    override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
        super.onClosing(webSocket, code, reason)

        val error = LunaError()
        error.code = -1
        error.error = Throwable(reason)

        mListener.onError(error)
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
        super.onFailure(webSocket, t, response)

        val error = LunaError()
        error.code = -1
        error.error = Throwable(response?.message())

        mListener.onError(error)
        L.error(error)
    }

    override fun onMessage(webSocket: WebSocket?, text: String?) {
        super.onMessage(webSocket, text)

        mListener.onMessage(text)
        L.fine(text!!)
    }
}