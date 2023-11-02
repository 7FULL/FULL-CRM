package com.full.crm.ui.theme.bills

import android.text.format.DateUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.full.crm.OptionsBar
import com.full.crm.models.Bill
import com.full.crm.network.API
import java.text.SimpleDateFormat
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bills(billsViewModel: BillsViewModel) {
    val bills: Array<Bill> = API.User.observeAsState().value?.getBills() ?: emptyArray()
    val clients: Array<String?> = API.User.observeAsState().value?.getClients()?.map { it.getName() }?.toTypedArray() ?: emptyArray()
    var text by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        bottomBar =
        {
            OptionsBar(modifier =
                Modifier.padding(top = 5.dp),
                selectedIcon = 1
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick =
            {/* TODO: Añadir el dialogo para crear una factura */},
                containerColor = Color(0xFF26A69A),
                contentColor = Color.White
            ){
            Text("+", fontSize = 30.sp)
        } },
    ) { padding  ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ){
            SearchBar(
                modifier = Modifier
                    .padding(top = 75.dp)
                    .background(color = Color.White),
                query = text,
                onQueryChange = { text = it },
                onSearch = { active = false },
                active = active,
                onActiveChange = {
                    active = it
                },
                placeholder = { Text("Nombre de la factura") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    Icon(
                        Icons.Default.Mic,
                        contentDescription = "Microphone",
                        modifier = Modifier
                            .clickable { /* TODO: Alomejor implementar la funcionalidad del microfono */ }
                    )
                },
            ) {
                //TODO: Poner las 5 facturas mas recientes
                repeat(4) { idx ->
                    val resultText = "Suggestion $idx"
                    ListItem(
                        headlineContent = { Text(resultText) },
                        supportingContent = { Text("Additional info") },
                        leadingContent = { Icon(Icons.Filled.AttachMoney, contentDescription = null) },
                        modifier = Modifier
                            .clickable {
                                text = resultText
                                active = false
                            }
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
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
                DropdownMenuBox(modifier = Modifier
                    .width(175.dp)
                    .align(alignment = Alignment.TopStart)
                    .padding(top = 175.dp), items = clients, placeholder = "Sin clientes")
                DropdownMenuBox(modifier = Modifier
                    .width(175.dp)
                    .align(alignment = Alignment.TopEnd)
                    .padding(top = 175.dp), items = clients, placeholder = "Sin clientes")
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .padding(top = 350.dp)
                        .requiredWidth(width = 360.dp)
                        .requiredHeight(height = 684.dp)
                ) {
                    //TODO: Crear el LazyColumn con los items de las facturas
                    LazyColumn(modifier = Modifier
                        .padding(bottom = 140.dp)
                        .fillMaxWidth()){
                        items(bills.size) { idx ->
                            Bill(bills[idx], modifier = Modifier
                                .fillMaxWidth())
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
fun Bill(bill: Bill, modifier: Modifier = Modifier) {
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
            }){

        Text(
            text = bill.getName()?: "Sin nombre",
            color = Color.Black,
            style = TextStyle(
                fontSize = 14.sp
            ),
            modifier = Modifier
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
                .padding(start = 50.dp)
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
                .padding(end = 50.dp)
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically)
        )

        Text(
            text = bill.getPrice().toString()+"€",
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
            color = if (bill.isPaid()) Color.Green else if (bill.getExpirationDate()!! < Calendar.getInstance().time) Color.Red else Color(
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
fun DropdownMenuBox(modifier: Modifier = Modifier, items: Array<String?>, placeholder: String?) {
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