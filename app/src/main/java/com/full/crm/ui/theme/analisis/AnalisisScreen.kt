package com.full.crm.ui.theme.agenda

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
import java.util.Date


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Analisis(modifier: Modifier = Modifier, agendyViewModel: AnalisisViewModel) {
    //Numero de pasos en el eje Y
    val steps = 5
    //Creamos unas gridlines personalizadas para que sean zigzag
    val gridLines = GridLines(
        color = Color(0xFFE0E0E0),
        lineWidth = 1.dp,
        pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(20f, 20f),
            phase = 0f
        ),
        alpha = 0.5f
    )

    //Obtenemos los datos de la vista modelo
    val bills = agendyViewModel.bills.value!!

    //Creamos una lista de puntos para la grafica
    val pointsData: MutableList<Point> = mutableListOf()

    //Creamos una lista para los ultimos 7 dias
    val last7Days: MutableList<Date> = mutableListOf()

    bills.forEachIndexed { index, bill ->
        if (index < 7 && bill.getExpirationDate() != null && bill.isPaid()) {
            last7Days.add(bill.getExpirationDate()!!)
        }
    }

    bills.forEachIndexed { index, bill ->
        if (bill.isPaid()) pointsData.add(Point(index.toFloat(), bill.getPrice()!!.toFloat()))
    }

    //Añadimos el punto de inicio
    pointsData.add(0, Point(0f, 0f))
    last7Days.add(0, Date())

    //Obtenemos la maxima cantidad de dinero que se ha pagado en un dia
    val max = pointsData.maxByOrNull { it.y }?.y

    //Obtenemos la minima cantidad de dinero que se ha pagado en un dia
    val min = pointsData.minByOrNull { it.y }?.y

    Scaffold(
        bottomBar =
        {
            OptionsBar(modifier = Modifier.padding(top = 5.dp),selectedIcon = 2)
        }
    ) { padding ->
        val xAxisData = AxisData.Builder()
            .axisStepSize(100.dp)
            .backgroundColor(Color(0xFFFFFFFF))
            .steps(pointsData.size - 1)
            .labelData { i ->
                if (i != 0){
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
                    ""
                }
            }.build()

        val lineChartData = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = pointsData,
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

        Box(modifier = Modifier.padding(top = 50.dp)) {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(250.dp),
                lineChartData = lineChartData
            )
        }

        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ){
            Box(
                modifier = Modifier
                    .requiredWidth(width = 360.dp)
                    .requiredHeight(height = 800.dp)
            ) {
                Text(
                    text = "ANÁLISIS",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .padding(top = 50.dp)
                        .background(color = Color.White)
                        .fillMaxWidth()
                )
            }
        }

        Row (modifier = Modifier.padding(top = 325.dp)){
            Column() {
                Text(
                    text = "Fecha",
                    textAlign = TextAlign.Center
                )
            }
            Column() {
                Text(
                    text = "Cantidad",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}