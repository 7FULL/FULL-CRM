package com.full.crm.ui.theme.agenda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.full.crm.OptionsBar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Analisis(modifier: Modifier = Modifier, analisisViewModel: AnalisisViewModel) {
    //Numero de pasos en el eje Y
    val steps = 5
    //Creamos unas gridlines personalizadas para que sean zigzag
    val gridLines = GridLines(
        color = Color(0xFFE0E0E0),
        lineWidth = 1.dp,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(25f, 20f),
            phase = 0f
        ),
        alpha = 0.5f
    )

    val coroutineScope = rememberCoroutineScope()

    val calendar = Calendar.getInstance()
    val milSec = calendar.timeInMillis
    val milSec7Days = milSec - 604800000

    val datePickerEmisionState =
        rememberDateRangePickerState(
            initialSelectedEndDateMillis = milSec,
            initialSelectedStartDateMillis = milSec7Days,
            initialDisplayMode = DisplayMode.Picker
        )

    val openDialogEmision = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)

    val dataPoints: Array<Point> by analisisViewModel.dataPoints.observeAsState(initial = emptyArray())
    val last7Days: MutableList<Date> by analisisViewModel.last7Days.observeAsState(initial = mutableListOf())

    //Obtenemos la maxima cantidad de dinero que se ha pagado en un dia
    val max = dataPoints.maxByOrNull { it.y }?.y

    //Obtenemos la minima cantidad de dinero que se ha pagado en un dia
    val min = dataPoints.minByOrNull { it.y }?.y

    Scaffold(
        bottomBar =
        {
            OptionsBar(modifier = Modifier.padding(top = 5.dp),selectedIcon = 2)
        }
    ) { padding ->
        val xAxisData = AxisData.Builder()
            .axisStepSize(100.dp)
            .backgroundColor(Color(0xFFFFFFFF))
            .steps(dataPoints.size-1)
            .labelData { i ->
                if(i != 0 && i != last7Days.size){
                    last7Days[i].date.toString() + "/" + (last7Days[i].month + 1).toString()
                }else{
                    ""
                }
            }
            .labelAndAxisLinePadding(15.dp)
            .build()

        val yAxisData = AxisData.Builder()
            .steps(steps)
            .backgroundColor(Color(0xFFFFFFFF))
            .labelAndAxisLinePadding(20.dp)
            .labelData { i ->
                if (i == 0) {
                    min.toString()
                } else if (i == steps ) {
                    max.toString()
                } else {
                    max!!.div(steps).times(i).toString()
                }
            }.build()

        val lineChartData = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = dataPoints.toList(),
                        LineStyle(color = Color(0xFF0095FF)),
                        //Estos son los puntos que se verian en las intersecciones de las lineas si no estuviesen transparentes
                        IntersectionPoint(color = Color(0x95FF)),
                        SelectionHighlightPoint(color = Color(0xFF0095FF)),
                        ShadowUnderLine(color = Color(0xFF0095FF)),
                        SelectionHighlightPopUp(
                            popUpLabel =
                            {
                                    _, y -> y.toString()
                            }
                        )
                    )
                ),
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            gridLines = gridLines,
            backgroundColor = Color.White
        )

        LazyColumn(){
            item {
                Box(contentAlignment = Alignment.TopCenter
                ){
                    Text(
                        text = "ANÁLISIS",
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .align(alignment = Alignment.TopCenter)
                            .background(color = Color.White)
                            .fillMaxWidth()
                    )
                }
            }

            item{
                Box(modifier = Modifier.padding(top = 0.dp)) {
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        lineChartData = lineChartData
                    )
                }
            }

            item{
                Box(modifier = Modifier
                    .padding(top = 25.dp, bottom = 25.dp)
                    .fillMaxWidth()){
                    TextField(
                        value = formatter.format(Date(datePickerEmisionState.selectedStartDateMillis?: 0) ) + " - " + formatter.format(Date(datePickerEmisionState.selectedEndDateMillis?: 0)),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon =
                        {
                            IconButton(
                                content = { Icon(Icons.Filled.DateRange, contentDescription = null) },
                                onClick = {
                                    showBottomSheet = true
                                    coroutineScope.launch {
                                        //openDialogEmision.show()
                                    }
                                }
                            )
                        },
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                    )
                }

                if(showBottomSheet){
                    ModalBottomSheet(
                        onDismissRequest =
                        {
                            showBottomSheet = false
                            analisisViewModel.updateDataPoints(
                                datePickerEmisionState.selectedStartDateMillis?: 0,
                                datePickerEmisionState.selectedEndDateMillis?: 0
                            )
                        } ,
                        sheetState = openDialogEmision)
                    {
                        DateRangePicker(
                            state = datePickerEmisionState,
                            headline = {
                            Text("Rango de fechas",
                                fontSize = 24.sp,
                                modifier = Modifier.padding(bottom = 20.dp, start = 20.dp)
                            )
                        })
                    }
                }
            }

            item{
                Box(modifier = Modifier.fillMaxWidth()){
                    Button(
                        onClick = { /*TODO: EXPORTAR*/ },
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff26A69A),
                            contentColor = Color.White,
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                    ) {
                        Text(text = "EXPORTAR")
                    }
                }
            }

            /*
            item{
                Spacer(modifier = Modifier.requiredHeight(height = 75.dp))
            }
            */
        }
    }
}

@Composable
@Preview
fun PreviewAnalisis() {
    Analisis(analisisViewModel =  AnalisisViewModel())
}