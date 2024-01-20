package com.suyash.creditmanager.domain.util

import java.time.format.DateTimeFormatter

enum class DateFormat(val format: String, val formatter: DateTimeFormatter) {
    DDMMYYYY("dd/MM/yyyy", DateTimeFormatter.ofPattern("dd/MM/yyyy")),
    MMDDYYYY("MM/dd/yyyy", DateTimeFormatter.ofPattern("MM/dd/yyyy")),
    YYYYMMDD("yyyy/MM/dd", DateTimeFormatter.ofPattern("yyyy/MM/dd")),
}