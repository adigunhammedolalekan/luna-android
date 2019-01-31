# luna-android
A simple android client wrapper for - [luna](https://github.com/adigunhammedolalekan/luna)

## Features
* Handled websocket connection
* Handled websocket auto-reconnect on error or io failure
* Handled channel subscription
* Handled channel unSubscription

## How to

* Clone project
`git clone https://github.com/adigunhammedolalekan/luna-android`

* Add module `luna` to your project
	`implementation project(':luna')`

* Set it up inside your app `Application.kt` class

``` Kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val config = Config.Builder()
                .setURL("ws://192.168.43.39:8009/ws/connect")
                .addHeader("Authorization", "Kick-Ass-Auth-Key")
                .addHeader("X-Client-Type", "Android")
                .build()
        Luna.create(config)
    }
}
```

In your activity/fragment or viewmodel? Create/subscribe to a channel and start listening for messages

``` Kotlin
private lateinit var channel: Channel

// activity onCreate
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_main)

    // create and subscribe to channel.
    // String parameter `/rooms/22/message` could be any channel path
    channel = Channel("/rooms/22/message")

    // Start listening for messages
    channel.messageListener = {data ->
        // data is the message payload encoded in JSON
        Log.d("Luna", "New Message => " + data)
    }
}
```

Send message to channel. You can send any object, it'll be converted to json automatically under the hood.

``` Kotlin
class Message (

            val message: String = "",
            val from: String = ""
    )
```
Send message to channel.
``` Kotlin
val message = Message("Hello, World!", "L3kanAdigun")
channel.sendMessage(message)
```

Follow me on Twitter [@L3kanAdigun](https://twitter.com/L3kanAdigun), for any question