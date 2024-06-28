package com.example.inohomdemo
import MainViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.inohomdemo.databinding.ActivityMainBinding
import com.example.inohomdemo.view.HomeActivity



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe WebSocketData LiveData for responses
        viewModel.webSocketData.observe(this) { response ->
            Log.d("WebSocket", "Received response: $response")
            if (response.contains("OnAuthenticated")) {
                Log.d("WebSocket", "Authentication successful")
                //     navigateToHomeActivity()
            }
        }

        val request = """
                {
                    "is_request": true,
                    "id": 8,
                    "params": [{"username": "demo", "password": "123456"}],
                    "method": "Authenticate"
                }
            """.trimIndent()
        viewModel.sendRequest(request)
        Log.d("WebSocket", "Login request sent")

        // Button click to send login request
        binding.accountBtn.setOnClickListener {
            viewModel.receiveResponse()


        }
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish current activity to prevent back navigation
        Log.d("WebSocket", "Navigating to HomeActivity")
    }
}
