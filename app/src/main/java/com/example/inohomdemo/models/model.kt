package com.example.inohomdemo.models

data class AuthRequest(
    val is_request: Boolean = true,
    val id: Int = 8,
    val params: List<AuthParams>,
    val method: String = "Authenticate"
)

data class AuthParams(
    val username: String,
    val password: String
)

data class AuthResponse(
    val id: Int,
    val params: List<String>,
    val method: String,
    val error: Any?,
    val is_request: Boolean
)


data class GetControlListRequest(
    val is_request: Boolean = true,
    val id: Int = 5,
    val params: List<Any> = listOf(mapOf<String, Any>()),
    val method: String = "GetControlList"
)

data class GetControlListResponse(
    val id: Int,
    val params: List<ControlListData>,
    val method: String,
    val error: Any?,
    val is_request: Boolean
)

data class ControlListData(
    val data: List<Control>
)

data class Control(
    val id: String,
    val name: String,
    val type_id: String,
    val bridge_device_id: String,
    val current_value: Int,
    val slot: Int,
    val is_active: Boolean,
    val temperature_settings: TemperatureSettings?,
    val area_id: String,
    val parameters: ControlParameters
)

data class TemperatureSettings(
    val has_heating: Boolean,
    val has_cooling: Boolean,
    val bridge_device_id: String,
    val virtual_control_id: String?,
    val input_id: String,
    val is_mode_heating: Boolean,
    val whole: Int,
    val fraction: Int
)

data class ControlParameters(
    val default_value: Int,
    val output_number: Int,
    val should_output_reverse: Boolean,
    val should_remember_last_value: Boolean,
    val end_time: String?,
    val start_time: String?,
    val is_notification: Boolean
)


data class UpdateControlValueRequest(
    val is_request: Boolean = true,
    val id: Int = 84,
    val params: List<UpdateControlParams>,
    val method: String = "UpdateControlValue"
)

data class UpdateControlParams(
    val id: String,
    val value: Int
)

data class UpdateControlValueResponse(
    val id: Int,
    val params: List<EntityUpdate>,
    val method: String,
    val error: Any?,
    val is_request: Boolean
)

data class EntityUpdate(
    val entity: Control,
    val type: String
)


