package com.example.inohomdemo.services

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*

class WebSocketService {

    private var client: HttpClient = HttpClient(CIO) {
        install(WebSockets)
    }
    private lateinit var session: WebSocketSession
    private var isConnected = false

    suspend fun connect(host: String, port: Int, path: String): Boolean {
        return try {
            session = client.webSocketSession(
                method = HttpMethod.Get,
                host = host,
                port = port,
                path = path
            )
            isConnected = true
            Log.d("WebSocketService", "WebSocket connected successfully")
            true
        } catch (e: Exception) {
            Log.e("WebSocketService", "WebSocket connection failed: ${e.message}", e)
            isConnected = false
            false
        }
    }

    suspend fun send(request: String) {
        if (isConnected) {
            try {
                session.send(Frame.Text(request))
                Log.d("WebSocketService", "Request sent: $request")



            } catch (e: Exception) {
                Log.e("WebSocketService", "Failed to send request: ${e.message}", e)
            }
        } else {
            Log.e("WebSocketService", "Cannot send request: WebSocket is not connected")
        }
    }

    suspend fun receive(): String? {
        return if (isConnected) {

            try {
                val frame :Frame= session.incoming.receive()
                when (frame) {
                    is Frame.Text -> {
                        val text = frame.readText()
                        Log.d("WebSocketService", "Response received: $text")
                        text
                    }
                    else -> null
                }
            } catch (e: Exception) {
                Log.e("WebSocketService", "Failed to receive response: ${e.message}", e)
                null
            }
        } else {
            Log.e("WebSocketService", "Cannot receive response: WebSocket is not connected")
            null
        }
    }

    suspend fun close() {
        if (isConnected) {
            try {
                session.close()
                client.close()
                isConnected = false
                Log.d("WebSocketService", "WebSocket closed successfully")
            } catch (e: Exception) {
                Log.e("WebSocketService", "Failed to close WebSocket: ${e.message}", e)
            }
        }
    }
}
