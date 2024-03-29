package com.full.crm.ui.theme.bills

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.crm.models.Bill
import com.full.crm.models.Client
import com.full.crm.network.API
import com.full.crm.network.BillRequest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Calendar
import java.util.Date

class BillsViewModel: ViewModel() {
    private val _bills = MutableLiveData<Array<Bill>>(API.User.value?.getBills()?.sortedByDescending { it.getEmisionDate() }?.toTypedArray() ?: emptyArray())

    val bills: LiveData<Array<Bill>> = _bills

    /**
     * Facturas que se muestran en pantalla
     */
    private val _auxBills = MutableLiveData<MutableList<Bill>>(_bills.value?.toMutableList() ?: mutableListOf())

    val auxBills: LiveData<MutableList<Bill>> = _auxBills

    /**
     * Facturas que se muestran en la barra de busqueda
     */
    private val _searchBills = MutableLiveData<MutableList<Bill>>(_bills.value?.toMutableList() ?: mutableListOf())

    val searchBills: LiveData<MutableList<Bill>> = _searchBills

    /**
     * Todos los clientes del usuario
     */
    var auxClients = API.User.value?.getClients()

    /**
     * Clientes que se muestran en el dropdown
     */
    val _clients = MutableLiveData<Array<String>>(API.User.value?.getClients()?.map { it.getName() ?: "" }?.toTypedArray() ?: emptyArray())

    /**
     * Los clientes originales sin modificar ninguno
     */
    private val _originalClients = MutableLiveData<Array<String>>(API.User.value?.getClients()?.map { it.getName() ?: "" }?.toTypedArray() ?: emptyArray())

    val originalClients: LiveData<Array<String>> = _originalClients

    val clients: LiveData<Array<String>> = _clients

    private val _searchBarText = MutableLiveData("")

    val searchBarText: LiveData<String> = _searchBarText

    private val _state = MutableLiveData("Todas")

    val state: LiveData<String> = _state

    private val _client = MutableLiveData("Todos")

    val client: LiveData<String> = _client


    //Datos para insertar factura
    private val _price = MutableLiveData("")
    val price: LiveData<String> = _price

    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    private val _clientName = MutableLiveData("")

    val clientName: LiveData<String> = _clientName

    //Booleano que indica si se esta editando una factura
    private val _editingBill = MutableLiveData(false)
    val editingBill: LiveData<Boolean> = _editingBill

    private val _emmited = MutableLiveData(false)
    val emmited: LiveData<Boolean> = _emmited

    //Factura que se esta editando
    private val _editingBillData = MutableLiveData<Bill>()
    val editingBillData: LiveData<Bill> = _editingBillData

    fun onBillFormChanged(price: String, name: String, clientName: String, emitted: Boolean){
        _price.value = price
        _name.value = name
        _clientName.value = clientName
        _emmited.value = emitted
    }

    fun addBill(expirationDate: Date, emissionDate: Date){
        val client = auxClients?.find { it.getName().equals(_clientName.value, true) }

        //Si el precio contiene una , la cambiamos por un .
        if (_price.value?.contains(",") == true){
            _price.value = _price.value?.replace(",", ".")
        }

        //Si contien un $ lo quitamos
        if (_price.value?.contains("$") == true){
            _price.value = _price.value?.replace("$", "")
        }

        //Si contiene un euro lo quitamos
        if (_price.value?.contains("€") == true){
            _price.value = _price.value?.replace("€", "")
        }

        //Si contiene un espacio lo quitamos
        if (_price.value?.contains(" ") == true){
            _price.value = _price.value?.replace(" ", "")
        }

        val bill = Bill(
            editingBillData.value?.getId() ?: "",
            emissionDate,
            expirationDate,
            BigDecimal(_price.value),
            false,
            _name.value,
            client?.getId() ?: "",
            _emmited.value ?: false,
            API.User.value!!.getId()
        )

        viewModelScope.launch {
            API.service.addBill(
                BillRequest(
                    API.employeeLogged!!, client?: Client(), bill
                )
            )
        }

        //Si no estamos editando una factura añadimos la factura a la lista sino la actualizamos
        if (!editingBill.value!!){
            _bills.value = _bills.value?.toMutableList()?.apply { add(0, bill) }?.toTypedArray()
        } else {
            _bills.value = _bills.value?.toMutableList()?.apply { set(indexOf(editingBillData.value), bill) }?.toTypedArray()
        }

        //Volvemos a filtrar las facturas por fecha
        _bills.value = _bills.value?.sortedByDescending { it.getEmisionDate() }?.toTypedArray()
        filterBills()

        Toast.makeText(API.mainActivity, "Factura añadida", Toast.LENGTH_SHORT).show()
    }

    //Esta funcion es la encargada de filtrar las facturas segun el filtro de busqueda el filtro de cliente y el filtro de estado
    fun filterBills(){
        var aux: MutableList<Bill> = mutableListOf()

        //Filtramos primero por busqueda
        val search = searchBarText.value ?: ""

        if (search.equals("", true)){
            aux = bills.value?.toMutableList() ?: mutableListOf()
        } else {
            for (bill in bills.value!!){
                if (bill.getName()?.contains(search, true) == true){
                    aux.add(bill)
                }
            }
        }

        //Si actualizamos aqui noi se apolican los demas filtros que es lo que queremos
        _searchBills.value = aux

        //Filtramos por cliente y si somos administradores filtramos por empleado
        if (API.isAdministrator){
            val client = client.value ?: ""

            if (client.equals("Todos", true)){
                aux = aux.toMutableList()
            } else {
                aux = aux.filter { it.getEmployeeID().equals(auxClients?.find { it.getName().equals(client, true) }?.getId(), true) }.toMutableList()
            }
        }else{
            val client = client.value ?: ""

            if (client.equals("Todos", true)){
                aux = aux.toMutableList()
            } else {
                aux = aux.filter { it.getClientID().equals(auxClients?.find { it.getName().equals(client, true) }?.getId(), true) }.toMutableList()
            }
        }

        //Filtramos por estado
        val state = state.value ?: ""

        if (state.equals("Pagada", true)){
            aux = aux.filter { it.isPaid() }.toMutableList()
        }else if (state.equals("Pendiente", true)){
            aux = aux.filter { !it.isPaid() && it.getExpirationDate()!! > Calendar.getInstance().time }.toMutableList()
        }else if (state.equals("Vencida", true)){
            aux = aux.filter { !it.isPaid() && it.getExpirationDate()!! < Calendar.getInstance().time }.toMutableList()
        }


        _auxBills.value = aux
    }

    fun initialize(){
        if (_clients.value?.isEmpty() == true){
            addClient("Sin clientes")
        } else {
            addClient("Todos")
        }

        if (bills.value?.size ?: 0 > 5){
            _searchBills.value = bills.value!!.copyOfRange(0, 5).toMutableList()
        } else {
            _searchBills.value = bills.value!!.toMutableList()
        }
    }

    fun onSearchBarTextChanged(text: String){
        _searchBarText.value = text
        filterBills()
    }

    fun onClientChanged(text: String){
        _client.value = text
        filterBills()
    }

    fun onStateChanged(text: String){
        _state.value = text
        filterBills()
    }

    fun onBillClicked(bill: Bill){
        Log.i("CRM", "Factura pulsada")

        //Si es administrador o la factura es del empleado actual
        if (API.isAdministrator){
            _editingBill.value = true
            _editingBillData.value = bill

            Log.i("CRM", bill.toString())
        } else {
            Toast.makeText(API.mainActivity, "No tienes permisos para editar facturas", Toast.LENGTH_SHORT).show()
        }
    }

    fun resetEditingBill(){
        _editingBill.value = false
        _editingBillData.value = null

        _price.value = ""
        _name.value = ""
        _clientName.value = ""

        _client.value = "Todos"
    }

    fun addClient(nombre: String) {
        _clients.value = _clients.value?.toMutableList()?.apply { add(0, nombre) }?.toTypedArray()
    }

    fun getClientName(id: String): String? {
        return auxClients?.find { it.getId().equals(id, true) }?.getName()
    }

    fun deleteBill(bill: Bill){
        viewModelScope.launch {
            API.service.deleteBill(
                bill.getId()!!
            )

            Toast.makeText(API.mainActivity, "Factura eliminada", Toast.LENGTH_SHORT).show()
        }

        _bills.value = _bills.value?.toMutableList()?.apply { remove(bill) }?.toTypedArray()
        _searchBills.value = _searchBills.value?.apply { remove(bill) }
        _auxBills.value = _auxBills.value?.toMutableList()?.apply { remove(bill) }?.toTypedArray()?.toMutableList()

        resetEditingBill()
    }
}