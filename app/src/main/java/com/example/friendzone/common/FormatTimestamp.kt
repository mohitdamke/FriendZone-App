package com.example.friendzone.common

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun FormatTimestamp(timestamp: String?): String {
    if (timestamp.isNullOrEmpty()) return "Unknown time"

    return try {
        // Assuming the timestamp is in UTC format like "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val dateTime = LocalDateTime.parse(timestamp, formatter)
        val now = LocalDateTime.now()
        val duration = Duration.between(dateTime, now)

        when {
            duration.toMinutes() < 1 -> "Just now"
            duration.toMinutes() < 60 -> "${duration.toMinutes()} mins ago"
            duration.toHours() < 24 -> "${duration.toHours()} hours ago"
            else -> "${duration.toDays()} days ago"
        }
    } catch (e: DateTimeParseException) {
        "Invalid time format"
    }
}
