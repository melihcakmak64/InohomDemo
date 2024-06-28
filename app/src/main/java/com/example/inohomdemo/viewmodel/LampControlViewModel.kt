package com.example.inohomdemo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inohomdemo.models.UpdateControlParams
import com.example.inohomdemo.models.UpdateControlValueRequest
import com.example.inohomdemo.models.UpdateControlValueResponse
import com.example.inohomdemo.service.WebSocketService
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LampControlViewModel : ViewModel() {
    private val webSocketService = WebSocketService.getInstance()
    private val _updateControlValueResponse = MutableLiveData<UpdateControlValueResponse?>()
    val updateControlValueResponse: LiveData<UpdateControlValueResponse?> = _updateControlValueResponse












    fun updateControlValue(controlId: String, value: Int) {
        viewModelScope.launch {
            if (webSocketService.isConnected()) {
                val request = UpdateControlValueRequest(params = listOf(UpdateControlParams(controlId, value)))
                Log.d("LampControlViewModel", "Sending updateControlValue request: $request")
                val responseJson = webSocketService.sendRequest(request)
                val response = Gson().fromJson(responseJson, UpdateControlValueResponse::class.java)
                _updateControlValueResponse.postValue(response)
                Log.d("LampControlViewModel", "Received updateControlValue response: $response")
            } else {
                Log.e("LampControlViewModel", "WebSocket is not connected")
            }
        }
    }

    fun connectWebSocket() {
        viewModelScope.launch {
            webSocketService.connect()
        }
    }
}