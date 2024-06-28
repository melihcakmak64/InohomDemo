package com.example.inohomdemo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inohomdemo.models.GetControlListRequest
import com.example.inohomdemo.models.GetControlListResponse
import com.example.inohomdemo.service.WebSocketService
import com.google.gson.Gson
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val webSocketService = WebSocketService.getInstance()
    private val _controlListResponse = MutableLiveData<GetControlListResponse?>()
    val controlListResponse: LiveData<GetControlListResponse?> = _controlListResponse

    fun getControlList() {
        viewModelScope.launch {
            if (webSocketService.isConnected()) {
                val request = GetControlListRequest()
                Log.d("HomeViewModel", "Sending getControlList request: $request")
                val responseJson = webSocketService.sendRequest(request)
                val response = Gson().fromJson(responseJson, GetControlListResponse::class.java)
                _controlListResponse.postValue(response)
                Log.d("HomeViewModel", "Received getControlList response: $response")
            } else {
                Log.e("HomeViewModel", "WebSocket is not connected")
            }
        }
    }

    fun connectWebSocket() {
        viewModelScope.launch {
            webSocketService.connect()
        }
    }
}