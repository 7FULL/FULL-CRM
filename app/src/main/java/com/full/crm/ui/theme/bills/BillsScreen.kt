package com.full.crm.ui.theme.bills

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.full.crm.OptionsBar
import com.full.crm.models.Bill
import com.full.crm.network.API
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


var state: String = "Todas"
var client: String = "Todos"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bills(billsViewModel: BillsViewModel) {
    val searchBarText: String by billsViewModel.searchBarText.observeAsState("")
    var searchBarActive by rememberSaveable { mutableStateOf(false) }
    val openAlertDialog = remember { mutableStateOf(false) }

    val auxBills: MutableList<Bill> by billsViewModel.auxBills.observeAsState(mutableListOf())
    val searchBills: MutableList<Bill> by billsViewModel.searchBills.observeAsState(mutableListOf())

    val clients: Array<String> by billsViewModel.clients.observeAsState(emptyArray())

    val formName: String by billsViewModel.name.observeAsState("")
    val formPrice: String by billsViewModel.price.observeAsState("")
    val formClient: String by billsViewModel.clientName.observeAsState("")

    val editingBill by billsViewModel.editingBill.observeAsState(false)
    val billEditing by billsViewModel.editingBillData.observeAsState(null)

    val calendar = Calendar.getInstance()
    val milSec = calendar.timeInMillis

    val emitted: Boolean by billsViewModel.emmited.observeAsState(false)

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)

    val datePickerEmisionState =
        rememberDatePickerState(
            initialSelectedDateMillis = milSec,
            initialDisplayMode = DisplayMode.Picker
        )

    val datePickerExpiracionState =
        rememberDatePickerState(
            initialSelectedDateMillis = milSec,
            initialDisplayMode = DisplayMode.Picker
        )

    billsViewModel.initialize()
    Scaffold(
        bottomBar =
        {
            OptionsBar(modifier =
                Modifier.padding(top = 5.dp),
                selectedIcon = 1
            )
        },

        floatingActionButton =
        {
            FloatingActionButton(onClick =
            {
                openAlertDialog.value = true
            },
                containerColor = Color(0xFF26A69A),
                contentColor = Color.White
            ){
                Text("+", fontSize = 30.sp)
            }
        },
    ) { padding ->
        when{
            openAlertDialog.value || editingBill -> {
                val openDialogEmision = remember { mutableStateOf(false) }
                val openDialogExpiración = remember { mutableStateOf(false) }

                val confirmDelete = remember { mutableStateOf(false) }

                if(editingBill){
                    val calendar = Calendar.getInstance()
                    calendar.time = billEditing?.getEmisionDate()!!
                    datePickerEmisionState.setSelection(calendar.timeInMillis)

                    calendar.time = billEditing?.getExpirationDate()!!
                    datePickerExpiracionState.setSelection(calendar.timeInMillis)

                    billsViewModel.onBillFormChanged(billEditing?.getPriceWithoutCurrency()?: "", billEditing?.getName()?: "", billEditing!!.getClientID()
                        ?.let { billsViewModel.getClientName(it) }
                        ?: "", billEditing?.isEmitted()?: false)
                }

                AlertDialog(
                    title = {
                        if (editingBill){
                            Text(text = "EDITAR FACTURA", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        } else {
                            Text(text = "EMITIR FACTURA", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                        }
                    },
                    text = {
                        Box(){

                            Box() {
                                Text(text = "Fecha de emisión:", modifier = Modifier.padding(start = 1.dp))
                                TextField(
                                    value = formatter.format(Date(datePickerEmisionState.selectedDateMillis!!)),
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon =
                                    {
                                        IconButton(
                                            content = { Icon(Icons.Filled.DateRange, contentDescription = null) },
                                            onClick = {
                                                openDialogEmision.value = true
                                            }
                                        )
                                    },
                                    modifier = Modifier.padding(top = 25.dp)
                                )
                            }

                            Box(modifier = Modifier.padding(top = 100.dp)) {
                                Text(text = "Fecha de expiración:", modifier = Modifier.padding(start = 1.dp))
                                TextField(
                                    value = formatter.format(Date(datePickerExpiracionState.selectedDateMillis!!)),
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon =
                                    {
                                        IconButton(
                                            content = { Icon(Icons.Filled.DateRange, contentDescription = null) },
                                            onClick = {
                                                openDialogExpiración.value = true
                                            }
                                        )
                                    },
                                    modifier = Modifier.padding(top = 25.dp)
                                )
                            }

                            Box(modifier = Modifier.padding(top = 200.dp)) {
                                Text(text = "Precio:", modifier = Modifier.padding(start = 1.dp))
                                TextField(
                                    value = formPrice,
                                    onValueChange = { billsViewModel.onBillFormChanged( it, formName, formClient, emitted) },
                                    readOnly = false,
                                    modifier = Modifier.padding(top = 25.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }

                            Box(modifier = Modifier.padding(top = 300.dp)) {
                                Text(text = "Nombre:", modifier = Modifier.padding(start = 1.dp))
                                TextField(
                                    value = formName,
                                    onValueChange = { billsViewModel.onBillFormChanged( formPrice, it, formClient, emitted) },
                                    readOnly = false,
                                    modifier = Modifier.padding(top = 25.dp)
                                )
                            }

                            Box(modifier = Modifier.padding(top = 400.dp)) {
                                Text(text = "Factura emitida:", modifier = Modifier.padding(start = 1.dp))
                                // Checkbox
                                Checkbox(checked = emitted, onCheckedChange = { billsViewModel.onBillFormChanged( formPrice, formName, formClient, it) },
                                    modifier = Modifier.padding(start = 100.dp).offset(x = 0.dp, y = (-12).dp)
                                )
                            }

                            if(!API.isAdministrator){
                                Box(modifier = Modifier.padding(top = 500.dp)) {
                                    Text(text = "Cliente:", modifier = Modifier.padding(start = 1.dp))
                                    DropdownMenuBox(items = billsViewModel.originalClients.value!!,
                                        placeholder = "Sin clientes",
                                        modifier = Modifier.padding(top = 25.dp),
                                        onValueChange = { billsViewModel.onBillFormChanged( formPrice, formName, it, emitted) },
                                    )
                                }
                            }

                            if(editingBill){
                                Box(modifier = Modifier.padding(top = 450.dp)) {
                                    Button(
                                        content = {
                                            Text(text = "Eliminar factura", modifier = Modifier.padding(start = 1.dp))
                                            Icon(Icons.Filled.DeleteForever, contentDescription = null)
                                                  },
                                        onClick = {
                                            if (confirmDelete.value){
                                                billsViewModel.deleteBill(billEditing!!)

                                                confirmDelete.value = false
                                                openAlertDialog.value = false
                                            } else {
                                                confirmDelete.value = true

                                                Toast.makeText(API.mainActivity, "Pulsa de nuevo para confirmar", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            when{ openDialogEmision.value ->
                                DatePickerDialog(
                                    onDismissRequest = {
                                        openDialogEmision.value = false
                                    },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                openDialogEmision.value = false
                                            }
                                        ) {
                                            Text("Confirmar")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = {
                                                openDialogEmision.value = false
                                            }
                                        ) {
                                            Text("Cancelar")
                                        }
                                    }
                                )
                                {
                                    DatePicker(state = datePickerEmisionState, headline = {
                                        Text("Fecha de emisión",
                                            fontSize = 24.sp,
                                            modifier = Modifier.padding(bottom = 20.dp, start = 20.dp)
                                        )
                                    })
                                }
                            }

                            when{ openDialogExpiración.value ->
                                DatePickerDialog(
                                    onDismissRequest = {
                                        openDialogExpiración.value = false
                                    },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                openDialogExpiración.value = false
                                            }
                                        ) {
                                           Text("Confirmar")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = {
                                                openDialogExpiración.value = false
                                            }
                                        ) {
                                            Text("Cancelar")
                                        }
                                    }
                                )
                                {
                                    DatePicker(state = datePickerExpiracionState, headline = {
                                        Text("Fecha de emisión",
                                            fontSize = 24.sp,
                                            modifier = Modifier.padding(bottom = 20.dp, start = 20.dp)
                                        )
                                    })
                                }
                            }
                        }
                    },
                    onDismissRequest = {
                        openAlertDialog.value = false
                        billsViewModel.resetEditingBill()
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                //Si la fecha de emision es anterior a la fecha de expiracion

                                if(datePickerEmisionState.selectedDateMillis?:milSec < datePickerExpiracionState.selectedDateMillis?:milSec){
                                    //Si todos los campos estan rellenos
                                    if(formPrice.isNotEmpty() && formName.isNotEmpty()){
                                        billsViewModel.addBill(Date(datePickerExpiracionState.selectedDateMillis?: milSec), Date(datePickerEmisionState.selectedDateMillis?: milSec))

                                        openAlertDialog.value = false
                                        billsViewModel.resetEditingBill()
                                    }else{
                                        Toast.makeText(API.mainActivity, "Debes rellenar todos los campos", Toast.LENGTH_LONG).show()
                                    }
                                }else{
                                    Toast.makeText(API.mainActivity, "La fecha de emisión no puede ser posterior a la fecha de expiración", Toast.LENGTH_LONG).show()
                                }
                            }
                        ) {
                            if (editingBill){
                                Text("Editar")
                            } else {
                                Text("Emitir")
                            }
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openAlertDialog.value = false
                                billsViewModel.resetEditingBill()
                            }
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ){
            SearchBar(
                modifier = Modifier
                    .padding(top = 75.dp)
                    .background(color = Color.White),
                query = searchBarText,
                onQueryChange = {
                                    billsViewModel.onSearchBarTextChanged(it)
                                },
                onSearch = {
                                searchBarActive = false

                                billsViewModel.filterBills()
                           },
                active = searchBarActive,
                onActiveChange = {
                    searchBarActive = it
                },
                placeholder = { Text("Nombre de la factura") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    Icon(
                        Icons.Default.Mic,
                        contentDescription = "Microphone",
                        modifier = Modifier
                            .clickable { /* TODO: Maybe implement the function of the microphone */ }
                    )
                },
            ) {
                if (searchBills != null){
                    repeat(searchBills.size) { idx ->
                        val resultText = searchBills[idx].getName() ?: "Esta factura no tiene nombre"
                        ListItem(
                            headlineContent = { Text(resultText) },
                            supportingContent = { Text(searchBills[idx].getPriceString()) },
                            leadingContent = { Icon(Icons.Filled.AttachMoney, contentDescription = null) },
                            modifier = Modifier
                                .clickable {
                                    searchBarActive = false

                                    billsViewModel.onSearchBarTextChanged(resultText)
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .requiredWidth(width = 360.dp)
                    .requiredHeight(height = 800.dp)
            ) {
                Text(
                    text = "FACTURAS",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .padding(top = 50.dp)
                        .background(color = Color.White)
                        .fillMaxWidth()
                )
                Box(){
                    Text(
                        text = if (API.isAdministrator) "Empleados" else "Clientes",
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .align(alignment = Alignment.TopCenter)
                            .padding(top = 185.dp, end = 125.dp)
                            .background(color = Color.White)
                    )
                    Text(
                        text = "Estado",
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .align(alignment = Alignment.TopCenter)
                            .padding(top = 185.dp, start = 240.dp)
                            .background(color = Color.White)
                    )
                }
                DropdownMenuBox(
                    modifier = Modifier
                        .width(175.dp)
                        .align(alignment = Alignment.TopStart)
                        .padding(top = 225.dp), items = clients,
                    placeholder = "Sin clientes"
                ) { value ->
                    client = value

                    billsViewModel.onClientChanged(client)
                }
                DropdownMenuBox(
                    modifier = Modifier
                        .width(175.dp)
                        .align(alignment = Alignment.TopEnd)
                        .padding(top = 225.dp), items = arrayOf("Todas", "Pagada", "Pendiente", "Vencida"),
                    placeholder = "Esto es un error"
                ) { value ->
                    state = value

                    billsViewModel.onStateChanged(state)
                }
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .padding(top = 450.dp)
                        .requiredWidth(width = 360.dp)
                        .requiredHeight(height = 684.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(bottom = 140.dp)
                            .fillMaxWidth()
                    )
                    {
                        if (auxBills.isEmpty()) item {
                            Text(
                                text = "No hay facturas",
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier
                                    .align(alignment = Alignment.Center)
                                    .padding(top = 50.dp)
                                    .background(color = Color.White)
                                    .fillMaxWidth()
                            )
                        }else{
                            item{
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .requiredHeight(height = 50.dp)
                                        .drawBehind {

                                            val strokeWidth = 1f * density
                                            val y = size.height - strokeWidth / 2

                                            drawLine(
                                                Color.Black,
                                                Offset(0f, y),
                                                Offset(size.width, y),
                                                strokeWidth
                                            )
                                        }
                                )
                                {

                                    Text(
                                        text = "Nombre",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        style = TextStyle(
                                            fontSize = 14.sp
                                        ),
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .wrapContentHeight(align = Alignment.CenterVertically)
                                    )

                                    Text(
                                        text = "Vencimiento",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        style = TextStyle(
                                            fontSize = 14.sp
                                        ),
                                        modifier = Modifier
                                            .align(alignment = Alignment.Center)
                                            .padding(start = 35.dp)
                                            .fillMaxHeight()
                                            .wrapContentHeight(align = Alignment.CenterVertically)
                                    )

                                    Text(
                                        text = "Emisión",
                                        color = Color.Black,
                                        style = TextStyle(
                                            fontSize = 14.sp
                                        ),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .align(alignment = Alignment.Center)
                                            .padding(end = 110.dp)
                                            .fillMaxHeight()
                                            .wrapContentHeight(align = Alignment.CenterVertically)
                                    )

                                    Text(
                                        text = "Precio",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        style = TextStyle(
                                            fontSize = 14.sp
                                        ),
                                        modifier = Modifier
                                            .align(alignment = Alignment.CenterEnd)
                                            .padding(end = 75.dp)
                                            .fillMaxHeight()
                                            .wrapContentHeight(align = Alignment.CenterVertically)
                                    )

                                    Text(
                                        text = "Estado",
                                        fontWeight = FontWeight.Bold,
                                        style = TextStyle(
                                            fontSize = 14.sp
                                        ),
                                        modifier = Modifier
                                            .align(alignment = Alignment.CenterEnd)
                                            .fillMaxHeight()
                                            .wrapContentHeight(align = Alignment.CenterVertically)
                                    )
                                }
                            }

                            items(auxBills.size) { idx ->
                                Bill(auxBills[idx], modifier = Modifier
                                    .fillMaxWidth(), billsViewModel)
                            }

                            /*
                            items(auxBills.size) { idx ->
                                Bill(auxBills[idx], modifier = Modifier
                                    .fillMaxWidth(), billsViewModel)
                            }
                            */
                        }
                    }
                }
                /*Box(
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .padding(bottom = 25.dp)
                        .requiredWidth(width = 263.dp)
                        .requiredHeight(height = 47.dp)
                        .background(color = Color(0xFF26A69A))
                ) {
                    Text(
                        text = "NUEVA FACTURA",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .fillMaxHeight()
                            .requiredWidth(width = 154.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                }*/
            }
        }
    }
}

@Composable
fun Bill(bill: Bill, modifier: Modifier = Modifier, billsViewModel: BillsViewModel) {
    var emissionDate: String = SimpleDateFormat("dd/MM").format(bill.getEmisionDate()!!)
    val expirationDate: String = SimpleDateFormat("dd/MM").format(bill.getExpirationDate()!!)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height = 50.dp)
            .drawBehind {

                val strokeWidth = 1f * density
                val y = size.height - strokeWidth / 2

                drawLine(
                    Color.Black,
                    Offset(0f, y),
                    Offset(size.width, y),
                    strokeWidth
                )
            }
            .clickable {
                billsViewModel.onBillClicked(bill)
            }
    )
    {

        Text(
            text =
            if(bill.getName()!!.length> 12){
                bill.getName()!!.substring(0, 12)+ "..."
            }else{
                bill.getName()?: "Sin nombre"
                 },
            color = Color.Black,
            style = TextStyle(
                fontSize = 14.sp
            ),
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
        )

        Text(
            text = expirationDate,
            color = Color.Black,
            style = TextStyle(
                fontSize = 14.sp
            ),
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .padding(start = 35.dp)
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
        )

        Text(
            text = emissionDate,
            color = Color.Black,
            style = TextStyle(
                fontSize = 14.sp
            ),
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .padding(end = 110.dp)
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
        )

        Text(
            text = bill.getPriceString(),
            color = Color.Black,
            style = TextStyle(
                fontSize = 14.sp
            ),
            modifier = Modifier
                .align(alignment = Alignment.CenterEnd)
                .padding(end = 75.dp)
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
        )

        Text(
            text = if (bill.isPaid()) "Pagada" else if (bill.getExpirationDate()!! < Calendar.getInstance().time) "Vencida" else "Pendiente",
            color = if (bill.isPaid()) Color(0xFF068A18) else if (bill.getExpirationDate()!! < Calendar.getInstance().time) Color.Red else Color(
                0xFFE6A23C
            ),
            style = TextStyle(
                fontSize = 14.sp
            ),
            modifier = Modifier
                .align(alignment = Alignment.CenterEnd)
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(modifier: Modifier = Modifier, items: Array<String>, placeholder: String, onValueChange: (String) -> Unit = {}){
    var aux = items

    if (items.isEmpty()) aux = arrayOf(placeholder)

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(aux[0]) }

    Box(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            selectedText?.let {
                TextField(
                    value = it,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                aux.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            if (item != null) {
                                Text(text = item)
                            }
                        },
                        onClick = {
                            selectedText = item
                            expanded = false
                            onValueChange(item ?: "")
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun BillsPreview() {
    Bills(BillsViewModel())
}