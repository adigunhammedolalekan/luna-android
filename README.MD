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

* Set it up inside your app `Application.kt` class

```Kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val config = Luna.Config()
        config.mUri = "ws://your.local.ip.address:port/ws/connect"
        Luna.create(config)
    }
}
```

In your activity/fragment or viewmodel? Create/subscribe to a channel and start listening for messages

```Kotlin
var mChannel: Channel? = null

//activity onCreate
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_main)

    mChannel = Channel("/rooms/22/message") //create and subscribe to channel. String parameter `/rooms/22/message` could be any channel path

    //Start listening for messages
    mChannel?.listener(object: Channel.Listener {
        override fun onMessage(data: String?) {

            Log.d("Luna", "New Message => " + data)
        }
    })
}
```

Send message to channel. You can send any object, it'll be converted to json automatically under the hood.

```Kotlin
class Message (

            val message: String = "",
            val from: String = ""
    )
```
Send message to channel.
```Kotlin
val message = Message("Hello, World!", "L3kanAdigun")
mChannel.sendMessage(message)
```

Follow me on Twitter [@L3kanAdigun](https://twitter.com/L3kanAdigun), my DM is open, come ask me questions!