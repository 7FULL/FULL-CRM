package com.full.crm.ui.theme.agenda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.full.crm.models.Appointment

class AgendyViewModel: ViewModel() {
    private val _appointments = MutableLiveData<Array<Appointment>>()
    val appointments: LiveData<Array<Appointment>> = _appointments
}