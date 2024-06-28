package com.example.inohomdemo.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import com.example.inohomdemo.R
import com.example.inohomdemo.databinding.FragmentLampControlBinding

// LampControlFragment.kt
class LampControlFragment : Fragment() {

    private var _binding: FragmentLampControlBinding? = null
    private val binding get() = _binding!!
    private var isLightBulbOn = mutableMapOf(
        R.id.icon_1 to false,
        R.id.icon_2 to false,
        R.id.icon_3 to false,
        R.id.icon_4 to false,
        R.id.icon_5 to false,
        R.id.icon_6 to false,
        R.id.icon_7 to false,
        R.id.icon_8 to false,
        R.id.icon_9 to false,
        R.id.icon_10 to false,
        R.id.icon_11 to false,
        R.id.icon_12 to false
    )



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLampControlBinding.inflate(inflater, container, false)
        setButtonListeners()
        initializeIcons()
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()

        }


        return binding.root
    }

    private fun setButtonListeners() {
        val buttonsAndIcons = listOf(
            binding.btn1 to binding.icon1,
            binding.btn2 to binding.icon2,
            binding.btn3 to binding.icon3,
            binding.btn4 to binding.icon4,
            binding.btn5 to binding.icon5,
            binding.btn6 to binding.icon6,
            binding.btn7 to binding.icon7,
            binding.btn8 to binding.icon8,
            binding.btn9 to binding.icon9,
            binding.btn10 to binding.icon10,
            binding.btn11 to binding.icon11,
            binding.btn12 to binding.icon12
        )

        for ((button, icon) in buttonsAndIcons) {
            button.setOnClickListener {
                val iconId = icon.id
                var isOn = !isLightBulbOn[iconId]!!
                isLightBulbOn[iconId]=isOn

                val drawableId = if (!isOn) {
                    R.drawable.ic_bulb_off
                } else {
                    R.drawable.ic_lightbulb
                }
                icon.foreground = resources.getDrawable(drawableId, null)
            }
        }
    }

    private fun initializeIcons() {
        val icons = listOf(
            binding.icon1,
            binding.icon2,
            binding.icon3,
            binding.icon4,
            binding.icon5,
            binding.icon6,
            binding.icon7,
            binding.icon8,
            binding.icon9,
            binding.icon10,
            binding.icon11,
            binding.icon12
        )

        for (icon in icons) {
            val isOn = isLightBulbOn[icon.id] ?: false
            val drawableId = if (isOn) {
                R.drawable.ic_lightbulb
            } else {
                R.drawable.ic_bulb_off
            }
            icon.foreground = resources.getDrawable(drawableId, null)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
