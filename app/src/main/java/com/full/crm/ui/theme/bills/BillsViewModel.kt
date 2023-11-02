package com.full.crm.ui.theme.bills

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.crm.models.Bill
import com.full.crm.models.Client
import com.full.crm.network.API
import com.full.crm.network.BillRequest
import kotlinx.coroutines.launch

class BillsViewModel: ViewModel() {
    private val _bills = MutableLiveData<Array<Bill>>()

    val bills: LiveData<Array<Bill>> = _bills

    fun addBill(){
        //TODO: Cambiar por los datos de los campos
        Log.i("CRM", API.employeeLogged?.getClients()?.get(0).toString())

        val client: Client = API.employeeLogged?.getClients()?.get(0) ?: Client()

        viewModelScope.launch {
            API.service.addBill(BillRequest(API.employeeLogged!!, client, Bill()))
        }
    }
}