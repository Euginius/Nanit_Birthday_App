package com.nanit.websocket.app.data

import kotlinx.serialization.Serializable

@Serializable
data class BirthdayInfo(
    val name: String? = null,
    val dob: Long = 0,
    val theme: String? = null
)