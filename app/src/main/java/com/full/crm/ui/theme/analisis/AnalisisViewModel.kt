package com.full.crm.ui.theme.agenda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.full.crm.models.Appointment
import com.full.crm.models.Bill
import com.full.crm.network.API

class AnalisisViewModel: ViewModel() {
    private val _bills = MutableLiveData<Array<Bill>>(API.User.value!!.getBills())
    val bills: LiveData<Array<Bill>> = _bills


}