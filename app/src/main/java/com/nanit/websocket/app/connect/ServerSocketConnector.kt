package com.nanit.websocket.app.connect

import com.nanit.websocket.app.data.BirthdayInfo
import com.nanit.websocket.app.data.ServerBirthdayInfo
import com.nanit.websocket.app.data.toBirthdayInfo
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

interface ServerSocketConnector {
   fun connectToServer(ipAddress: String,  onResponseReceived: (ServerResult) -> Unit)
}

class ServerSocketConnectorImpl : ServerSocketConnector {
    companion object {
        private const val SERVER_PATH = "nanit"
        private const val BIRTHDAY_COMMAND = "HappyBirthday"
        private const val DEFAULT_TIMEOUT = 5L
    }

    private var currentWebSocket: WebSocket? = null

    // Create OkHttp client
    private val client = OkHttpClient.Builder()
        .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        .build()

    override fun connectToServer(ipAddress: String, onResponseReceived: (ServerResult) -> Unit) {
        currentWebSocket?.close(1000, "New connection requested")

        // Create request
        val request = Request.Builder()
            .url("ws://$ipAddress:8080/$SERVER_PATH")
            .build()

        // Connect to WebSocket
        val webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                // Send the message to request birthday info
                webSocket.send(BIRTHDAY_COMMAND)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    // Parse the received JSON using Kotlinx Serialization
                    val result = Json.decodeFromString<ServerBirthdayInfo>(text)
                    onResponseReceived.invoke(ServerResult(Status.SUCCESS,result.toBirthdayInfo()))
                } catch (e: Exception) {
                    onResponseReceived.invoke(ServerResult(Status.SUCCESS,null))
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                onResponseReceived.invoke(ServerResult(Status.FAILED,null))
            }
        }
        client.newWebSocket(request, webSocketListener)
    }
}

enum class Status {
    SUCCESS,FAILED
}

data class ServerResult(val status: Status, val info : BirthdayInfo?)