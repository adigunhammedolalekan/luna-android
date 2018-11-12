package com.luna.chat.luna

import com.google.gson.annotations.SerializedName

class WsMessage<T> (
        @SerializedName("action")
        val action: String,

        @SerializedName("path")
        val path: String,

        @SerializedName("data")
        val data: T
)