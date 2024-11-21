package com.andrii_a.muze.ui.util

import com.andrii_a.muze.domain.models.Artist
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

val Artist.lifeYearsString: String
    get() {
        val dateFormat = LocalDate.Format {
            dayOfMonth()
            char('.')
            monthNumber()
            char('.')
            year()
        }

        val born = LocalDate.parse(input = this.bornDateString.orEmpty()).format(dateFormat)

        val died = LocalDate.parse(input = this.diedDateString.orEmpty()).format(dateFormat)

        return "$born - $died"
    }