package com.example.inohomdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.inohomdemo.databinding.ActivityMainBinding
import com.example.inohomdemo.view.HomeActivity
import com.example.inohomdemo.viewmodel.MainViewModel



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        setContentView(binding.root)



        binding.accountBtn.setOnClickListener {
            val username = "demo"
            val password ="123456"
            viewModel.authenticate(username, password)
        }

        viewModel.authResponse.observe(this, Observer { response ->
            if (response != null && response.method.equals("OnAuthenticated")) {
                Log.d("MainActivity", "Authentication success")


                navigateToHomeActivity()
            } else {
                Log.e("MainActivity", "Authentication failed")
            }
        })
    }



    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
        Log.d("WebSocket", "Navigating to HomeActivity")
    }


}
