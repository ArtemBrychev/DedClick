package com.example.dedclick.service

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    private val moscowZone = ZoneId.of("Europe/Moscow")

    @RequiresApi(Build.VERSION_CODES.O)
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    @RequiresApi(Build.VERSION_CODES.O)
    private val fullFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatSignalTime(isoString: String): String {
        val dateTime = LocalDateTime.parse(isoString)

        val zoned = dateTime
            .atZone(ZoneId.of("UTC"))
            .withZoneSameInstant(moscowZone)

        val now = LocalDateTime.now(moscowZone)

        val date = zoned.toLocalDate()
        val today = now.toLocalDate()
        val yesterday = today.minusDays(1)

        val time = zoned.format(timeFormatter)

        return when (date) {
            today -> "сегодня в $time"
            yesterday -> "вчера в $time"
            else -> zoned.format(fullFormatter)
        }
    }
}