package com.example.inohomdemo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inohomdemo.models.*
import com.example.inohomdemo.service.WebSocketService
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val webSocketService = WebSocketService.getInstance()

    private val _authResponse = MutableLiveData<AuthResponse?>()
    val authResponse: LiveData<AuthResponse?> = _authResponse
    init {
        connectWebSocket()
    }





    fun connectWebSocket() {
        GlobalScope.launch {
            webSocketService.connect()
        }
    }

    fun authenticate(username: String, password: String) {
        viewModelScope.launch {
            if (webSocketService.isConnected()) {
                val request = AuthRequest(params = listOf(AuthParams(username, password)))
                Log.d("MainViewModel", "Sending authenticate request: $request")
                val responseJson = webSocketService.sendRequest(request)
                val response = Gson().fromJson(responseJson, AuthResponse::class.java)
                _authResponse.postValue(response)
                Log.d("MainViewModel", "Received authenticate response: $response")
            } else {
                Log.e("MainViewModel", "WebSocket is not connected")
            }
        }
    }




}
