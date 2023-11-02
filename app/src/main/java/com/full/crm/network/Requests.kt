package com.full.crm.network

import com.full.crm.models.Bill
import com.full.crm.models.Client
import com.full.crm.models.Employee

data class BillRequest(
    val employee: Employee,
    val client: Client,
    val bill: Bill
)