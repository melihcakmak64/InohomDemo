package com.example.inohomdemo.service
import android.util.Log
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.ws
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.withContext


class WebSocketService private constructor() {

    private val client = HttpClient {
        install(WebSockets)
    }
    private var isConnected = false

    companion object {
        @Volatile
        private var INSTANCE: WebSocketService? = null

        fun getInstance(): WebSocketService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WebSocketService().also { INSTANCE = it }
            }
        }
    }

    suspend fun connect() {
        withContext(Dispatchers.IO) {
            try {
                client.ws(host = "85.104.58.133", port = 8080, path = "/ws") {
                    isConnected = true
                    Log.d("WebSocketService", "Connected to WebSocket")
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val response = frame.readText()
                            Log.d("WebSocketService", "Received: $response")
                            // Handle response
                        }
                    }
                }
            } catch (e: ClosedReceiveChannelException) {
                Log.e("WebSocketService", "WebSocket closed: ${e.message}")
                isConnected = false
                reconnect()
            } catch (e: Exception) {
                Log.e("WebSocketService", "Error in WebSocket connection: ${e.message}")
                isConnected = false
                reconnect()
            }
        }
    }

    private suspend fun reconnect() {
        if (!isConnected) {
            Log.d("WebSocketService", "Reconnecting to WebSocket...")
            connect()
        }
    }

    suspend fun sendRequest(request: Any): String {
        return withContext(Dispatchers.IO) {
            var response = ""
            if (isConnected) {
                client.ws(host = "85.104.58.133", port = 8080, path = "/ws") {
                    val requestJson = Gson().toJson(request)
                    Log.d("WebSocketService", "Sending request: $requestJson")

                    send(Frame.Text(requestJson))
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            response = frame.readText()
                            Log.d("WebSocketService", "Received response: $response")
                            break
                        }
                    }
                }
            }
            response
        }
    }

    fun isConnected(): Boolean {
        return isConnected
    }
}
