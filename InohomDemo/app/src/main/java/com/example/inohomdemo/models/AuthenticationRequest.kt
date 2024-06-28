package com.example.inohomdemo.models

data class AuthenticationRequest(
    val is_request: Boolean = true,
    val id: Int,
    val params: List<Map<String, String>>,
    val method: String
)