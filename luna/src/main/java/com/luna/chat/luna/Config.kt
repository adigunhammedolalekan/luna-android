package com.luna.chat.luna

class Config {
    private var URL: String = ""
    private var headers = hashMapOf<String, String>()

    class Builder() {

        private var URL: String = ""
        private var headers = hashMapOf<String, String>()

        fun setURL(url: String): Builder {
            URL = url
            return this
        }

        fun addHeader(key: String, header: String): Builder {
            headers[key] = header
            return this
        }

        fun build(): Config {

            val c = Config()
            c.URL = URL
            c.headers = headers
            return c
        }
    }

    fun getURL() = URL

    fun getHeaders() = headers
}