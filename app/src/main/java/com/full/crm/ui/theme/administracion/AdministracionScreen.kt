package com.full.crm.ui.theme.administracion

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.full.crm.OptionsBar
import com.full.crm.models.Appointment
import com.full.crm.models.Bill
import com.full.crm.models.Client
import com.full.crm.models.Employee
import com.full.crm.navigation.NavigationManager
import com.full.crm.ui.theme.login.Body
import com.full.crm.ui.theme.login.Input

@Composable
fun Administracion(modifier: Modifier = Modifier, administracionViewModel: AdministracionViewModel) {
    val isLoading: Boolean by administracionViewModel.isLoading.observeAsState(initial = false)

    if (isLoading){
        Box(
            Modifier
                .fillMaxSize()
        ) {
            CircularProgressIndicator(
                Modifier
                    .align(Alignment.Center)
                    .size(50.dp))
        }
    }else{
        AdministationBody(administracionViewModel = administracionViewModel)
    }
}

@Composable
fun AdministationBody(administracionViewModel: AdministracionViewModel){
    val tipo = remember { mutableIntStateOf(-1) }

    val editEmployeeDialog by administracionViewModel.editEmployeeDialog.observeAsState(initial = false)

    Scaffold(
        bottomBar =
        {
            OptionsBar(modifier =
            Modifier.padding(top = 5.dp),
                selectedIcon = 4
            )
        },
    ) { padding ->
        if (tipo.value == -1){
            Box(modifier = Modifier
                .padding(padding)
                .fillMaxSize()){
                //Hacemos 2 cuadrados en medio que pongan: Clientes, Empleados, Facturas, Citas

                Column(modifier = Modifier.align(Alignment.Center)){
                    Row{
                        Column(modifier = Modifier.clickable { tipo.value = 0 }) {
                            Box(modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                                .background(
                                    color = Color(0xFF1976D2),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            ) {
                                Icon(Icons.Default.Person, contentDescription = "Clientes",
                                    modifier = Modifier
                                        .size(75.dp)
                                        .align(Alignment.TopCenter),
                                    tint = Color.White)
                                Text(text = "CLIENTES", modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 10.dp),
                                    fontSize = 16.sp,
                                    color = Color.White)
                            }
                        }
                        Column(modifier = Modifier.clickable { tipo.value = 1 }) {
                            Box(modifier = Modifier
                                .width(110.dp)
                                .height(100.dp)
                                .padding(start = 10.dp)
                                .background(
                                    color = Color(0xFF26A69A),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            ) {
                                Icon(Icons.Default.SupervisorAccount, contentDescription = "EMPLEADOS",
                                    modifier = Modifier
                                        .size(75.dp)
                                        .align(Alignment.TopCenter),
                                    tint = Color.White)
                                Text(text = "EMPLEADOS", modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 10.dp),
                                    fontSize = 16.sp,
                                    color = Color.White)
                            }
                        }
                    }

                    /*
                    Row(modifier = Modifier.padding(top = 10.dp)){
                        Column(modifier = Modifier.clickable { tipo.value = 2 }) {
                            Box(modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                                .background(
                                    color = Color(0xFF3ED8F8),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            ) {
                                Icon(Icons.Rounded.AccountBalanceWallet, contentDescription = "FACTURAS",
                                    modifier = Modifier
                                        .size(75.dp)
                                        .padding(5.dp)
                                        .align(Alignment.TopCenter),
                                    tint = Color.White)
                                Text(text = "FACTURAS", modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 10.dp),
                                    fontSize = 16.sp,
                                    color = Color.White)
                            }
                        }
                        Column(modifier = Modifier.clickable { tipo.value = 3 }) {
                            Box(modifier = Modifier
                                .width(110.dp)
                                .height(100.dp)
                                .padding(start = 10.dp)
                                .background(
                                    color = Color(0xFFFF2C55),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            ) {
                                Icon(Icons.Default.DateRange, contentDescription = "CITAS",
                                    modifier = Modifier
                                        .size(75.dp)
                                        .padding(7.dp)
                                        .align(Alignment.TopCenter),
                                    tint = Color.White)
                                Text(text = "CITAS", modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 10.dp),
                                    fontSize = 16.sp,
                                    color = Color.White)
                            }
                        }
                    }*/
                }
            }
        }else if(tipo.value == 0){
            ClientsAdmin(administracionViewModel = administracionViewModel)
        }else if(tipo.value == 1){
            EmployeesAdmin(administracionViewModel = administracionViewModel)
        }else if(tipo.value == 2){
            BillsAdmin(administracionViewModel = administracionViewModel)
        }else{
            AppointmentsAdmin(administracionViewModel = administracionViewModel)
        }
    }
}

@Composable
fun ClientsAdmin(administracionViewModel: AdministracionViewModel){
    val editClient: Client by administracionViewModel.editClient.observeAsState(initial = Client())
    val clients: Array<Client> by administracionViewModel.clients.observeAsState(initial = emptyArray())
    val editClientDialog by administracionViewModel.editClientDialog.observeAsState(initial = false)

    val confirmDelete = remember { mutableStateOf(false) }

    val name by administracionViewModel.clientName.observeAsState(initial = "")
    val surname by administracionViewModel.clientSurname.observeAsState(initial = "")
    val email by administracionViewModel.clientEmail.observeAsState(initial = "")
    val phone by administracionViewModel.clientPhone.observeAsState(initial = "")

    administracionViewModel.clientName.value = editClient.getName() ?: ""
    administracionViewModel.clientSurname.value = editClient.getSurname() ?: ""
    administracionViewModel.clientEmail.value = editClient.getEmail() ?: ""
    administracionViewModel.clientPhone.value = editClient.getPhone() ?: ""

    Text(text = "CLIENTES", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(), fontSize = 40.sp)
    LazyColumn(
        content =
        {
            items(clients.size) {
                ClientItem(client = clients[it], administracionViewModel = administracionViewModel)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
    )
    Box(modifier = Modifier.fillMaxHeight(0.9f),
        contentAlignment = Alignment.BottomStart
    ) {
        FloatingActionButton(onClick = { NavigationManager.instance!!.navigate("administration") },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)) {
            Text(text = "<--")
        }
        FloatingActionButton(onClick = { administracionViewModel.editClient(Client(), false) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .padding(start = 335.dp)) {
            Text(text = "+")
        }

        if (editClientDialog){
            AlertDialog(
                onDismissRequest = { administracionViewModel.closeEditClientDialog() },
                title = { Text(text = "Editar cliente") },
                text = {
                    Column {
                        Input(label = "Nombre",
                            placeholder = "Nombre",
                            onInputChanged = { administracionViewModel.clientName.value = it },
                            value = name)
                        Spacer(modifier = Modifier.height(40.dp))
                        Input(label = "Apellidos",
                            placeholder = "Apellidos",
                            onInputChanged = { administracionViewModel.clientSurname.value = it },
                            value = surname)
                        Spacer(modifier = Modifier.height(40.dp))
                        Input(label = "Email",
                            placeholder = "Email",
                            onInputChanged = { administracionViewModel.clientEmail.value = it },
                            value = email)
                        Spacer(modifier = Modifier.height(40.dp))
                        Input(label = "Teléfono",
                            placeholder = "Teléfono",
                            onInputChanged = { administracionViewModel.clientPhone.value = it },
                            value = phone)
                        Spacer(modifier = Modifier.height(40.dp))

                        if(editClient.getId() != null){
                            Button(onClick = {
                                if (confirmDelete.value){
                                    administracionViewModel.deleteClient(editClient)
                                    NavigationManager.instance!!.navigate("administration")
                                }else{
                                    confirmDelete.value = true
                                    Toast.makeText(NavigationManager.instance!!.context, "Pulsa otra vez para confirmar", Toast.LENGTH_SHORT).show()
                                }
                            }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2C55))) {
                                Text(text = "Borrar")
                                Icon(Icons.Default.DeleteForever, contentDescription = "")
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        administracionViewModel.addClient()
                        administracionViewModel.closeEditClientDialog()
                    }) {
                        Text(text = "Guardar")
                    }
                },
                dismissButton = {
                    Button(onClick = { administracionViewModel.closeEditClientDialog() }) {
                        Text(text = "Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun ClientItem(client: Client, administracionViewModel: AdministracionViewModel){
    Row(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .background(color = Color(0xFFE0E0E0), shape = RoundedCornerShape(10.dp))
        .clickable {
            administracionViewModel.editClient(client)
        }) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = "Nombre: ${client.getName()}")
            Text(text = "Apellidos: ${client.getSurname()}")
            Text(text = "Teléfono: ${client.getPhone()}")
            Text(text = "Email: ${client.getEmail()}")
        }
    }
}

@Composable
fun EmployeesAdmin(administracionViewModel: AdministracionViewModel){
    val editEmployee: Employee by administracionViewModel.editEmployee.observeAsState(initial = Employee())
    val employees: Array<Employee> by administracionViewModel.employees.observeAsState(initial = emptyArray())
    val editEmployeeDialog by administracionViewModel.editEmployeeDialog.observeAsState(initial = false)

    val confirmDelete = remember { mutableStateOf(false) }

    val name by administracionViewModel.employeeName.observeAsState(initial = "")
    val surname by administracionViewModel.employeeSurname.observeAsState(initial = "")
    val email by administracionViewModel.employeeEmail.observeAsState(initial = "")
    val phone by administracionViewModel.employeePhone.observeAsState(initial = "")

    administracionViewModel.employeeName.value = editEmployee.getName() ?: ""
    administracionViewModel.employeeSurname.value = editEmployee.getSurname() ?: ""
    administracionViewModel.employeeEmail.value = editEmployee.getEmail() ?: ""
    administracionViewModel.employeePhone.value = editEmployee.getPhone() ?: ""

    Text(text = "EMPLEADOS", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),fontSize = 40.sp)
    LazyColumn(
        content =
        {
            items(employees.size) {
                EmployeeItem(employee = employees[it], administracionViewModel = administracionViewModel)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
    )
    Box(modifier = Modifier.fillMaxHeight(0.9f),
        contentAlignment = Alignment.BottomStart
    ) {
        FloatingActionButton(onClick = { NavigationManager.instance!!.navigate("administration") },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)) {
            Text(text = "<--")
        }
        FloatingActionButton(onClick = { administracionViewModel.editEmployee(Employee(), false) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .padding(start = 335.dp)) {
            Text(text = "+")
        }

        if (editEmployeeDialog) {
            AlertDialog(
                onDismissRequest = { administracionViewModel.closeEditEmployeeDialog() },
                title = { Text(text = "Editar empleado") },
                text = {
                    Column {
                        Input(
                            label = "Nombre",
                            placeholder = "Nombre",
                            onInputChanged = { administracionViewModel.employeeName.value = it },
                            value = name
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Input(
                            label = "Apellidos",
                            placeholder = "Apellidos",
                            onInputChanged = { administracionViewModel.employeeSurname.value = it },
                            value = surname
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Input(
                            label = "Email",
                            placeholder = "Email",
                            onInputChanged = { administracionViewModel.employeeEmail.value = it },
                            value = email
                        )
                        Spacer(modifier = Modifier.height(40.dp))
                        Input(
                            label = "Teléfono",
                            placeholder = "Teléfono",
                            onInputChanged = { administracionViewModel.employeePhone.value = it },
                            value = phone
                        )
                        Spacer(modifier = Modifier.height(40.dp))

                        if (editEmployee.getId() != null) {
                            Button(
                                onClick = {
                                    if (confirmDelete.value) {
                                        administracionViewModel.deleteEmployee(editEmployee)
                                        NavigationManager.instance!!.navigate("administration")
                                    } else {
                                        confirmDelete.value = true
                                        Toast.makeText(
                                            NavigationManager.instance!!.context,
                                            "Pulsa otra vez para confirmar",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF2C55)
                                )
                            ) {
                                Text(text = "Borrar")
                                Icon(Icons.Default.DeleteForever, contentDescription = "")
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        administracionViewModel.addEmployee()
                        administracionViewModel.closeEditEmployeeDialog()
                    }) {
                        Text(text = "Guardar")
                    }
                },
                dismissButton = {
                    Button(onClick = { administracionViewModel.closeEditEmployeeDialog() }) {
                        Text(text = "Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun EmployeeItem(employee: Employee, administracionViewModel: AdministracionViewModel){
    Row(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .background(color = Color(0xFFE0E0E0), shape = RoundedCornerShape(10.dp))
        .clickable {
            administracionViewModel.editEmployee(employee)
        }) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = "Nombre: ${employee.getName()}")
            Text(text = "Apellidos: ${employee.getSurname()}")
            Text(text = "Teléfono: ${employee.getPhone()}")
            Text(text = "Email: ${employee.getEmail()}")
        }
    }
}

@Composable
fun BillsAdmin(administracionViewModel: AdministracionViewModel){
    val bills: Array<Bill> by administracionViewModel.bills.observeAsState(initial = emptyArray())

    Text(text = "FACTURAS", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),fontSize = 40.sp)
    LazyColumn(
        content =
        {
            items(bills.size) {
                BillItem(bill = bills[it])
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
    )
    Box(modifier = Modifier.fillMaxHeight(0.9f),
        contentAlignment = Alignment.BottomStart
    ) {
        FloatingActionButton(onClick = { NavigationManager.instance!!.navigate("administration") },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)) {
            Text(text = "<--")
        }
    }
}

@Composable
fun BillItem(bill: Bill){
    Row(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .background(color = Color(0xFFE0E0E0), shape = RoundedCornerShape(10.dp))
        .clickable { /*TODO*/ }) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = "Nombre: ${bill.getName()}")
            Text(text = "Cliente: ${bill.getClientID()}")
            Text(text = "Empleado: ${bill.getEmployeeID()}")
            Text(text = "Fecha de emision: ${bill.getEmisionDate()}")
            Text(text = "Fecha de expiracion: ${bill.getExpirationDate()}")
            Text(text = "Precio: ${bill.getPriceString()}")
        }
    }
}

@Composable
fun AppointmentsAdmin(administracionViewModel: AdministracionViewModel){
    val appointments: Array<Appointment> by administracionViewModel.appointments.observeAsState(initial = emptyArray())

    Text(text = "CITAS", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),fontSize = 40.sp)
    LazyColumn(
        content =
        {
            items(appointments.size) {
                AppointmentItem(appointment = appointments[it])
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
    )
    Box(modifier = Modifier.fillMaxHeight(0.9f),
        contentAlignment = Alignment.BottomStart
    ) {
        FloatingActionButton(onClick = { NavigationManager.instance!!.navigate("administration") },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)) {
            Text(text = "<--")
        }
    }
}

@Composable
fun AppointmentItem(appointment: Appointment){
    Row(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .background(color = Color(0xFFE0E0E0), shape = RoundedCornerShape(10.dp))
        .clickable { /*TODO*/ }) {
        Column(modifier = Modifier.padding(10.dp)) {
            /*Text(text = "Cliente: ${appointment.getClientID()}")
            Text(text = "Empleado: ${appointment.getEmployeeID()}")
            Text(text = "Fecha: ${appointment.getDate()}")
            Text(text = "Hora: ${appointment.getTime()}")*/
        }
    }
}