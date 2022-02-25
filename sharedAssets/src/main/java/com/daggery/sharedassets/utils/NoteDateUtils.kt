package com.daggery.sharedassets.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class NoteDateUtils {

    companion object {
        private val fullFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    }

    fun getRawCurrentDate(): Long {
        val date = LocalDateTime.now()
        return date.format(fullFormatter).toLong()
    }

    fun getParsedDate(rawDate: Long): String {
        val parsedDate = fullFormatter.parse(rawDate.toString())
        return dateFormatter.format(parsedDate)
    }
}