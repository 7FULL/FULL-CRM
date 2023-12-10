package com.full.crm.models

import com.full.crm.utils.IsoDateTimeSerializer
import com.google.gson.annotations.JsonAdapter
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Date
import java.util.Locale


class Bill
    (
    private val id: String?,
    @JsonAdapter(IsoDateTimeSerializer::class)
    private val emissionDate: Date?,
    @JsonAdapter(IsoDateTimeSerializer::class)
    private val expirationDate: Date?,//Se utiliza bigdecimal para evitar problemas con los decimales (es lo que se recomienda para el dinero)
    private val price: BigDecimal?,
    private val paid: Boolean,
    private val name: String?,
    private val clientID: String?,
    private val employeeID: String?
) {

        //Constructores
        constructor() : this(null, null, null, null, false, null, null, null)
        constructor(emissionDate: Date?, expirationDate: Date?, price: BigDecimal?, paid: Boolean, name: String?, clientID: String?, employeeID: String?) : this(null, emissionDate, expirationDate, price, paid, name, clientID, employeeID)

    //Getters
    fun getId(): String? {
        return id
    }

    fun getClientID(): String? {
        return clientID
    }

    fun getEmployeeID(): String? {
        return employeeID
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

    fun getPriceString(): String{
        val displayVal: BigDecimal = this.price?.setScale(2, RoundingMode.HALF_EVEN) ?: BigDecimal(0)
        val eurCostFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE)
        eurCostFormat.minimumFractionDigits = 1
        eurCostFormat.maximumFractionDigits = 2
        return eurCostFormat.format(displayVal.toDouble())
    }

    fun getPriceWithoutCurrency(): String{
        val displayVal: BigDecimal = this.price?.setScale(2, RoundingMode.HALF_EVEN) ?: BigDecimal(0)
        return displayVal.toString()
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