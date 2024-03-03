package com.full.crm.ui.theme.proyectos

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.full.crm.OptionsBar
import com.full.crm.R
import com.full.crm.network.API
import com.google.gson.Gson
import java.util.*

data class Proyecto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val fechaInicio: String,
    val fechaFin: String,
    val estatus: String,
    val prioridad: String,
)

// Lista de proyectos de muestra
//Convertimos la lista de proyectos a un string en formato JSON
val proyectosDeMuestra = Gson().fromJson(API.mainActivity!!.getSharedPreferences("proyectos", Context.MODE_PRIVATE).getString("proyectos", "[]"), Array<Proyecto>::class.java).toList()

val opcionesDePrioridad = listOf("Alta", "Media", "Baja")
val opcionesDeEstatus = listOf("En progreso", "Completado", "Pendiente")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProyectosScreen() {
    var showDialog by remember { mutableStateOf(false) }
    var proyectoAEditar by remember { mutableStateOf<Proyecto?>(null) }
    var proyectosList by remember { mutableStateOf(proyectosDeMuestra) }

    Scaffold (
        bottomBar = {
            OptionsBar(modifier =
            Modifier.padding(top = 5.dp),
                selectedIcon = 4
            )
        }
    ){ innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            // Background image

            Column(modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(26.dp)) {
                Text(
                    "Proyectos",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        proyectoAEditar = null
                        showDialog = true
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Agregar Nuevo Proyecto", color = MaterialTheme.colorScheme.onPrimary)
                }

                if (showDialog){
                    ModalBottomSheet(onDismissRequest = { showDialog = false }) {
                        NuevoProyectoDialog(
                            proyecto = proyectoAEditar,
                            onDismiss = {
                                showDialog = false
                                proyectoAEditar = null
                            },
                            onSave = { proyecto ->
                                //Lo guardamos en el local storage
                                API.mainActivity!!.getSharedPreferences("proyectos", Context.MODE_PRIVATE).edit().apply {
                                    //Convertimos la lista de proyectos a un string en formato JSON
                                    Gson().toJson(proyectosList + proyecto).also { putString("proyectos", it) }

                                    apply()
                                }

                                if (proyectoAEditar == null) {
                                    proyectosList = proyectosList + proyecto
                                } else {
                                    proyectosList = proyectosList.map { if (it.id == proyecto.id) proyecto else it }
                                }
                                showDialog = false
                                proyectoAEditar = null
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(proyectosList) { proyecto ->
                        ProyectoItem(
                            proyecto = proyecto,
                            onEdit = {
                                proyectoAEditar = proyecto
                                showDialog = true
                            },
                            onDelete = {
                                proyectosList = proyectosList.filterNot { it.id == proyecto.id }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NuevoProyectoDialog(onDismiss: () -> Unit, onSave: (Proyecto) -> Unit, proyecto: Proyecto?) {
    var nombre by remember { mutableStateOf(proyecto?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(proyecto?.descripcion ?: "") }
    var fechaInicio by remember { mutableStateOf(proyecto?.fechaInicio ?: "") }
    var fechaFin by remember { mutableStateOf(proyecto?.fechaFin ?: "") }
    var estatus by remember { mutableStateOf(proyecto?.estatus ?: opcionesDeEstatus.first()) }
    var prioridad by remember { mutableStateOf(proyecto?.prioridad ?: opcionesDePrioridad.first()) }
    Surface(
        modifier = Modifier.padding(26.dp).padding(start = 40.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text("Agregar Nuevo Proyecto", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
            OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })

            DatePickerField(label = "Fecha de Inicio", fecha = fechaInicio, onFechaSeleccionada = { fechaInicio = it })
            DatePickerField(label = "Fecha de Fin", fecha = fechaFin, onFechaSeleccionada = { fechaFin = it })

            DropdownMenuSelector(label = "Prioridad", opciones = opcionesDePrioridad, seleccionActual = prioridad, onSeleccion = { prioridad = it })
            DropdownMenuSelector(label = "Estatus", opciones = opcionesDeEstatus, seleccionActual = estatus, onSeleccion = { estatus = it })

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
                TextButton(
                    onClick = {
                        onSave(Proyecto(
                            id = proyecto?.id ?: (Math.random() * 1000).toInt(),
                            nombre = nombre,
                            descripcion = descripcion,
                            fechaInicio = fechaInicio,
                            fechaFin = fechaFin,
                            estatus = estatus,
                            prioridad = prioridad,
                        ))
                    }
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}

@Composable
fun DatePickerField(label: String, fecha: String, onFechaSeleccionada: (String) -> Unit, context: Context = LocalContext.current) {
    val calendar = Calendar.getInstance()
    if (fecha.isNotEmpty()) {
        // Assuming fecha is in format YYYY-MM-DD
        val parts = fecha.split("-").map { it.toInt() }
        calendar.set(parts[0], parts[1] - 1, parts[2])
    }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        DatePickerDialog(
            context, { _, selectedYear, selectedMonth, selectedDay ->
                onFechaSeleccionada("$selectedYear-${selectedMonth + 1}-${selectedDay}")
                showDialog.value = false
            }, year, month, day
        ).show()
    }

    OutlinedTextField(
        value = fecha,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        trailingIcon = {
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Date", Modifier.clickable { showDialog.value = true })
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuSelector(
    label: String,
    opciones: List<String>,
    seleccionActual: String,
    onSeleccion: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Mejora de accesibilidad y usabilidad
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        seleccionActual?.let {
            TextField(
                value = it,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )
        }

        // Implementación de un menú desplegable con scroll si es necesario
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onSeleccion(opcion)
                        expanded = false
                    }
                )
            }
        }
    }
}



@Composable
fun ProyectoItem(proyecto: Proyecto, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = proyecto.nombre, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Descripción: ${proyecto.descripcion}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Inicio: ${proyecto.fechaInicio} - Fin: ${proyecto.fechaFin}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Estatus: ${proyecto.estatus}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Prioridad: ${proyecto.prioridad}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = onEdit) {
                    Text("Editar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Borrar")
                }
            }
        }
    }

}
