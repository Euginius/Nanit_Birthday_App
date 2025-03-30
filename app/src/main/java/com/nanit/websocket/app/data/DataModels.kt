package com.nanit.websocket.app.data

import kotlinx.serialization.Serializable

@Serializable
data class ServerBirthdayInfo(
    val name: String? = null,
    val dob: Long = 0,
    val theme: String? = null
)

@Serializable
data class BirthdayInfo(
    val name: String? = null,
    val dob: Long = 0,
    val theme: ThemeType = ThemeType.FOX
)

enum class ThemeType {
    FOX, PELICAN,ELEPHANT
}

fun ServerBirthdayInfo.toBirthdayInfo(): BirthdayInfo {
    return BirthdayInfo(
        name = this.name,
        dob = this.dob,
        theme = when (this.theme?.uppercase()) {
            "fox" -> ThemeType.FOX
            "elephant" -> ThemeType.ELEPHANT
            "pelican" -> ThemeType.PELICAN
            else -> ThemeType.FOX // Default value if theme is null or not recognized
        }
    )
}