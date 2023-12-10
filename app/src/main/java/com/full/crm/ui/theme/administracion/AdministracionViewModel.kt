package com.full.crm.ui.theme.administracion

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.crm.models.Appointment
import com.full.crm.models.Bill
import com.full.crm.models.Client
import com.full.crm.models.Employee
import com.full.crm.navigation.NavigationManager
import com.full.crm.network.API
import com.full.crm.ui.theme.bills.client
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



    private val _editEmployee = MutableLiveData<Employee>()
    val editEmployee: LiveData<Employee> = _editEmployee

    private val _editClient = MutableLiveData<Client>()
    val editClient: LiveData<Client> = _editClient

    private val _editEmployeeDialog = MutableLiveData<Boolean>(false)
    val editEmployeeDialog: LiveData<Boolean> = _editEmployeeDialog

    private val _editClientDialog = MutableLiveData<Boolean>(false)
    val editClientDialog: LiveData<Boolean> = _editClientDialog


    val clientName = MutableLiveData<String>("")
    val clientSurname = MutableLiveData<String>("")
    val clientEmail = MutableLiveData<String>("")
    val clientPhone = MutableLiveData<String>("")

    val employeeName = MutableLiveData<String>("")
    val employeeSurname = MutableLiveData<String>("")
    val employeeEmail = MutableLiveData<String>("")
    val employeePhone = MutableLiveData<String>("")

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

    fun deleteEmployee(employee: Employee){
        viewModelScope.launch {
            API.service.deleteEmployee(employee.getId() ?: "")
            loadData()
        }
    }

    fun deleteClient(client: Client){
        viewModelScope.launch {
            API.service.deleteClient(client.getId() ?: "")
            loadData()
        }
    }

    fun addEmployee(){
        val employee = editEmployee.value ?: Employee()

        employee.setName(employeeName.value ?: "")
        employee.setSurname(employeeSurname.value ?: "")
        employee.setEmail(employeeEmail.value ?: "")
        employee.setPhone(employeePhone.value ?: "")

        //Validar datos
        if(employee.getName()!!.isEmpty()){
            Toast.makeText(API.mainActivity, "El nombre no puede estar vacio", Toast.LENGTH_SHORT).show()
        }else if(employee.getSurname()!!.isEmpty()){
            Toast.makeText(API.mainActivity, "El apellido no puede estar vacio", Toast.LENGTH_SHORT).show()
        } else if(employee.getEmail()!!.isEmpty()){
            Toast.makeText(API.mainActivity, "El email no puede estar vacio", Toast.LENGTH_SHORT).show()
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(employee.getEmail()).matches()){
            Toast.makeText(API.mainActivity, "El email no es valido", Toast.LENGTH_SHORT).show()
        } else if(employee.getPhone()!!.isEmpty()){
            Toast.makeText(API.mainActivity, "El telefono no puede estar vacio", Toast.LENGTH_SHORT).show()
        }else{
            viewModelScope.launch {
                API.service.addEmployee(employee)
                NavigationManager.instance!!.navigate("administration")
            }
        }
    }

    fun addClient(){

        val client = editClient.value ?: Client()

        client.setName(clientName.value ?: "")
        client.setSurname(clientSurname.value ?: "")
        client.setEmail(clientEmail.value ?: "")
        client.setPhone(clientPhone.value ?: "")

        //Validar datos
        if(client.getName()!!.isEmpty()){
            Toast.makeText(API.mainActivity, "El nombre no puede estar vacio", Toast.LENGTH_SHORT).show()
        }else if(client.getSurname()!!.isEmpty()){
            Toast.makeText(API.mainActivity, "El apellido no puede estar vacio", Toast.LENGTH_SHORT).show()
        }else if(client.getEmail()!!.isEmpty()){
            Toast.makeText(API.mainActivity, "El email no puede estar vacio", Toast.LENGTH_SHORT).show()
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(client.getEmail()).matches()){
            Toast.makeText(API.mainActivity, "El email no es valido", Toast.LENGTH_SHORT).show()
        }else if(client.getPhone()!!.isEmpty()){
            Toast.makeText(API.mainActivity, "El telefono no puede estar vacio", Toast.LENGTH_SHORT).show()
        }else{
            viewModelScope.launch {
                API.service.addClient(client)
                NavigationManager.instance!!.navigate("administration")
            }
        }
    }


    fun editEmployee(employee: Employee, edit: Boolean = true){
        if(edit){
            _editEmployee.value = employee
        }else{
            _editEmployee.value = Employee()

            employeeName.value = ""
            employeeSurname.value = ""
            employeeEmail.value = ""
            employeePhone.value = ""
        }

        _editEmployeeDialog.value = true
    }

    fun editClient(client: Client, edit: Boolean = true){
        if(edit){
            _editClient.value = client
        }else{
            clientName.value = ""
            clientSurname.value = ""
            clientEmail.value = ""
            clientPhone.value = ""

            _editClient.value = Client()
        }

        _editClientDialog.value = true
    }

    fun closeEditEmployeeDialog(){
        _editEmployeeDialog.value = false
    }

    fun closeEditClientDialog(){
        _editClientDialog.value = false
    }
}