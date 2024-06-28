package com.example.inohomdemo.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.inohomdemo.databinding.ActivityHomeBinding
import com.example.inohomdemo.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(binding.root)

        binding.btnLight.setOnClickListener {
            viewModel.getControlList()


        }

        viewModel.controlListResponse.observe(this, Observer { response ->
            if (response != null && response.method.equals("GetControlList")) {
                navigateLampControlFragment()
            } else {
                Log.e("HomeActivity", "Request failed")
            }
        })



    }

    fun navigateLampControlFragment(){
        val fragment = LampControlFragment()
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .addToBackStack(null)
            .commit()

    }
}