package com.nanit.websocket.app.utils

import android.util.Log
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Date

object Utils {

    fun getAge(birthTimestamp: Long): AgeResult {
        try {
            // Check for invalid timestamps (negative or zero)
            if (birthTimestamp <= 0) {
                throw IllegalArgumentException("Invalid birthdate")
            }

            // Get the current date
            val currentDate = LocalDate.now()

            // Convert timestamp to LocalDate (birthdate)
            val birthDate = Date(birthTimestamp).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            // Check if the birthdate is in the future
            if (birthDate.isAfter(currentDate)) {
                throw IllegalArgumentException("Birthdate cannot be in the future")
            }

            // Calculate age as a Period between the current date and the birthdate
            val period = Period.between(birthDate, currentDate)

            // Return age as months or years based on the difference
            return if (period.years == 0) {
                AgeResult(period.months, true) // Age in months
            } else {
                AgeResult(period.years, false) // Age in years
            }

        } catch (e: Exception) {
            Log.e("AgeCalculation", "Error calculating age: ${e.message}")
            throw e // Re-throw or handle error as needed
        }
    }
}

data class AgeResult(val number: Int, val isMonths: Boolean)