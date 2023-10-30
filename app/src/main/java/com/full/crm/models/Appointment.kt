package com.full.crm.models

import android.annotation.SuppressLint
import android.util.Log
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class Appointment {
    private var date: String? = null
    private var clientID: String? = null

    fun Appointment() {}

    fun Appointment(date: Date?, clientID: String?) {
        this.date = date.toString()

        this.clientID = clientID
    }

    override fun toString(): String {
        return "Appointment(date=$date, clientID=$clientID)"
    }
}