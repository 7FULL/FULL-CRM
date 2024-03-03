package com.example.proyectostareas.vistas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.full.crm.R
import java.util.Calendar
import kotlin.random.Random


data class Tarea(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    var estaCompletada: Boolean

)

val tareasDeMuestra = listOf(
    Tarea(id = 1, titulo = "Tarea 1", descripcion = "Descripción de la tarea 1", estaCompletada = false),
    Tarea(id = 2, titulo = "Tarea 2", descripcion = "Descripción de la tarea 2", estaCompletada = false),
    Tarea(id = 3, titulo = "Tarea 3", descripcion = "Descripción de la tarea 3", estaCompletada = false),
    // Agrega más tareas de muestra aquí...
)

@Composable
fun TareasScreen() {
    val tareas = remember { mutableStateListOf(*tareasDeMuestra.toTypedArray()) }
    var tareaAEditar by remember { mutableStateOf<Tarea?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var mostrarCalendario by remember { mutableStateOf(false) } // Para controlar la visibilidad del calendario
    val fechaSeleccionada = remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(20.dp)) {
            Text("Pantalla de Tareas", style = MaterialTheme.typography.headlineMedium)

            Button(
                onClick = {
                    tareaAEditar = null
                    mostrarDialogo = true
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Agregar Tarea")
            }

            // Botón para mostrar el calendario
            Button(
                onClick = { mostrarCalendario = true },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Seleccionar Fecha")
            }

            if (mostrarCalendario) {
                // Función para mostrar el DatePicker y actualizar la fecha
                MostrarDatePicker { fecha ->
                    fechaSeleccionada.value = fecha
                    mostrarCalendario = false // Ocultar el DatePicker después de seleccionar una fecha
                }
            }

            // Opcionalmente, mostrar la fecha seleccionada
            if (fechaSeleccionada.value.isNotEmpty()) {
                Text("Fecha seleccionada: ${fechaSeleccionada.value}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(tareas) { tarea ->
                    TareaItem(
                        tarea = tarea,
                        onEditar = {
                            tareaAEditar = tarea
                            mostrarDialogo = true
                        },
                        onBorrar = {
                            tareas.remove(tarea)
                        },
                        onCompletadaCambio = { completada ->
                            val index = tareas.indexOf(tarea)
                            if (index != -1) {
                                tareas[index] = tarea.copy(estaCompletada = completada)
                            }
                        }
                    )
                }
            }
        }
    }

    if (mostrarDialogo) {
        DialogoAgregarEditarTarea(tarea = tareaAEditar) { resultado ->
            resultado?.let {
                if (tareaAEditar == null) {
                    tareas.add(it)
                } else {
                    val index = tareas.indexOfFirst { t -> t.id == it.id }
                    if (index != -1) {
                        tareas[index] = it
                    }
                }
                tareaAEditar = null
                mostrarDialogo = false
            }
        }
    }
}

@Composable
fun DialogoAgregarEditarTarea(tarea: Tarea?, onResultado: (Tarea?) -> Unit) {
    var titulo by rememberSaveable { mutableStateOf(tarea?.titulo ?: "") }
    var descripcion by rememberSaveable { mutableStateOf(tarea?.descripcion ?: "") }
    AlertDialog(
        onDismissRequest = { onResultado(null) },
        title = { Text(if (tarea == null) "Agregar Tarea" else "Editar Tarea") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") }
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val nuevaTarea = Tarea(
                        id = tarea?.id ?: Random.nextInt(),
                        titulo = titulo,
                        descripcion = descripcion,
                        estaCompletada = tarea?.estaCompletada ?: false
                    )
                    onResultado(nuevaTarea)
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = { onResultado(null) }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun TareaItem(
    tarea: Tarea,
    onEditar: () -> Unit,
    onBorrar: () -> Unit,
    onCompletadaCambio: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = tarea.estaCompletada, onCheckedChange = onCompletadaCambio)
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        ) {
            Text(tarea.titulo, style = MaterialTheme.typography.titleMedium)
            Text(tarea.descripcion, style = MaterialTheme.typography.bodyMedium)
            // Mostrar "Sin asignar" si responsable es null
        }
        IconButton(onClick = onEditar) {
            Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Color.White)
        }
        IconButton(onClick = onBorrar) {
            Icon(Icons.Filled.Delete, contentDescription = "Borrar", tint = Color.White)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostrarDatePicker(onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Formato de fecha seleccionada, ajusta según necesidad
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            onDateSelected(selectedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}
