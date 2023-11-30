package com.full.crm.ui.theme.administracion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.crm.models.Appointment
import com.full.crm.models.Bill
import com.full.crm.models.Client
import com.full.crm.models.Employee
import com.full.crm.network.API
import kotlinx.coroutines.launch

class AdministracionViewModel: ViewModel() {

    private val _employees = MutableLiveData<Array<Employee>>()
    val employees: LiveData<Array<Employee>> = _employees

    private val _clients = MutableLiveData<Array<Client>>()
    val clients: LiveData<Array<Client>> = _clients

    private val _bills = MutableLiveData<Array<Bill>>()
    val bills: LiveData<Array<Bill>> = _bills

    private val _appointments = MutableLiveData<Array<Appointment>>()
    val appointments: LiveData<Array<Appointment>> = _appointments


    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading : LiveData<Boolean> = _isLoading

    init {
        loadData()
    }

    fun loadData(){
        viewModelScope.launch {
            _isLoading.value = true

            _employees.value = API.service.getEmployees().body()?.data ?: emptyArray()
            _clients.value = API.service.getClients().body()?.data ?: emptyArray()
            _bills.value = API.service.getBills().body()?.data ?: emptyArray()
            _appointments.value = API.service.getAppointments().body()?.data ?: emptyArray()

            _isLoading.value = false
        }
    }
}