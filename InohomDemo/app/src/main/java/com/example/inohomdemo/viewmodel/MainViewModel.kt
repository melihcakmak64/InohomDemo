import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inohomdemo.services.WebSocketService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val webSocketService = WebSocketService()
    private val _webSocketData = MutableLiveData<String>()
    val webSocketData: LiveData<String> get() = _webSocketData
    private var isWebSocketConnected = false
    private val requestQueue = mutableListOf<String>()

    init {
        connectToWebSocket()
    }

    private fun connectToWebSocket() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isWebSocketConnected = webSocketService.connect("85.104.58.133", 8080, "/ws")
                if (isWebSocketConnected) {
                    processRequestQueue()
                } else {
                    Log.e("WebSocket", "Connection failed")
                }
            } catch (e: Exception) {
                Log.e("WebSocket", "Connection error: ${e.message}", e)
            }
        }
    }

    private fun processRequestQueue() {
        synchronized(requestQueue) {
            while (requestQueue.isNotEmpty()) {
                val request = requestQueue.removeAt(0)
                sendRequest(request)
            }
        }
    }

    fun sendRequest(request: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (isWebSocketConnected) {
                    webSocketService.send(request)
                    Log.d("WebSocket", "Request sent: $request")
                } else {
                    // WebSocket not connected, queue the request
                    synchronized(requestQueue) {
                        requestQueue.add(request)
                    }
                }
            } catch (e: Exception) {
                Log.e("WebSocket", "Sending request failed: ${e.message}", e)
            }
        }
    }

    fun receiveResponse() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                while (true) {
                    val response = webSocketService.receive()
                    Log.d("WebSocket", "Response received: $response")

                    response?.let {
                        _webSocketData.postValue(it)
                        Log.d("WebSocket", "Response received: $it")
                    }
                }
            } catch (e: Exception) {
                Log.e("WebSocket", "Receiving response failed: ${e.message}", e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                webSocketService.close()
                Log.d("WebSocket", "Connection closed")
            } catch (e: Exception) {
                Log.e("WebSocket", "Closing connection failed: ${e.message}", e)
            }
        }
    }
}
