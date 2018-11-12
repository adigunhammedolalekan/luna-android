package com.luna.chat.lunachat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.luna.chat.luna.Channel
import kotlinx.android.synthetic.main.layout_main.*

class MainActivity : AppCompatActivity() {

    var mChannel: Channel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)

        var message: String = ""
        mChannel = Channel("/rooms/22/message")

        mChannel?.listener(object: Channel.Listener {
            override fun onMessage(data: String?) {

                tv_message.text = ""
                message += data
                tv_message.text = message
            }
        })

        btn_send.setOnClickListener {

            val text = edt_message.text
            if (!text.isEmpty()) {

                val newMessage = Message(text.toString(), "lunaAndroid")
                mChannel?.sendMessage(newMessage)
                edt_message.text.clear()
            }
        }
    }

    class Message (

            val message: String = "",
            val from: String = ""
    )
}