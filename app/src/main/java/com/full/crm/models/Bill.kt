package com.full.crm.models

import com.full.crm.network.IsoDateTimeSerializer
import com.google.gson.annotations.JsonAdapter
import java.math.BigDecimal
import java.util.Date

class Bill {
    private val id: String? = null

    @JsonAdapter(IsoDateTimeSerializer::class)
    private val emissionDate: Date? = null

    @JsonAdapter(IsoDateTimeSerializer::class)
    private val expirationDate: Date? = null

    //Se utiliza bigdecimal para evitar problemas con los decimales (es lo que se recomienda para el dinero)
    private val price: BigDecimal? = null

    private val paid = false

    private val name: String? = null

    private val clientID: String? = null

    private val employeeID: String? = null

    //Getters
    fun getId(): String? {
        return id
    }

    fun getEmisionDate(): Date? {
        return emissionDate
    }

    fun getExpirationDate(): Date? {
        return expirationDate
    }

    fun getPrice(): BigDecimal? {
        return price
    }

    fun isPaid(): Boolean {
        return paid
    }

    fun getName(): String? {
        return name
    }

    //To string
    @Override
    override fun toString(): String {
        return "Bill{" +
                "id='" + id + '\'' +
                ", emisionDate=" + emissionDate +
                ", expirationDate=" + expirationDate +
                ", price=" + price +
                ", paid=" + paid +
                ", name='" + name + '\'' +
                ", clientID='" + clientID + '\'' +
                '}'
    }
}