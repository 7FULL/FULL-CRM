package com.full.crm.ui.theme.agenda

import android.util.Half.toFloat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.yml.charts.common.model.Point
import com.full.crm.models.Appointment
import com.full.crm.models.Bill
import com.full.crm.network.API
import java.util.Calendar
import java.util.Date

class AnalisisViewModel: ViewModel() {
    private val bills: Array<Bill>? = API.User.value!!.getBills()

    //Creamos una lista de puntos para la grafica
    val pointsDataPayments: MutableList<Point> = mutableListOf()

    //Creamos una lista de puntos para la grafica de las perdidas
    val pointsDataLosses: MutableList<Point> = mutableListOf()

    //Creamos una lsita de puntos para la grafica de las ganancias (pagos - perdidas)
    val pointsDataGains: MutableList<Point> = mutableListOf()

    //Creamos una lista para los ultimos 7 dias
    private val _last7Days = MutableLiveData<MutableList<Date>>(mutableListOf())
    val last7Days: LiveData<MutableList<Date>> = _last7Days

    private val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

    private val _dataPoints = MutableLiveData<Array<Point>>(initialize())
    val dataPoints: LiveData<Array<Point>> = _dataPoints

    fun initialize(): Array<Point>{
        //Añadimos el punto de inicio
        pointsDataPayments.add(0, Point(0f, 0f))
        pointsDataGains.add(0, Point(0f, 0f))
        pointsDataLosses.add(0, Point(0f, 0f))

        _last7Days.value!!.add(0, Date())

        //Obtenemos los ultimos 7 dias en los que se haya emitido una factura
        bills!!.forEach {
            val date = it.getEmisionDate()!!
            val calendar = Calendar.getInstance()
            calendar.time = date

            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)

            if (month == currentMonth) {
                if (day >= currentDay - 15) {
                    _last7Days.value!!.add(date)
                }
            }
        }

        bills.forEachIndexed { index, bill ->
            //Si la factura se ha emitido en los ultimos 7 dias
            if(_last7Days.value!!.contains(bill.getEmisionDate())) {
                if (bill.isPaid()) pointsDataPayments.add(Point(pointsDataPayments.size.toFloat(), bill.getPrice()!!.toFloat()))

                if (!bill.isPaid()) pointsDataLosses.add(Point(pointsDataLosses.size.toFloat(), bill.getPrice()!!.toFloat()))

                if (bill.isPaid()) pointsDataGains.add(Point(pointsDataGains.size.toFloat(), bill.getPrice()!!.toFloat()))

                if (!bill.isPaid()) pointsDataGains.add(Point(pointsDataGains.size.toFloat(), -bill.getPrice()!!.toFloat()))
            }
        }

        /*
        //Imprimimos los puntos de la grafica
        pointsDataGains.forEach {
            Log.i("CRM", "x: " + it.x + " y: " + it.y)
        }

        //Imprimimos los 7 dias
        _last7Days.value!!.forEach {
            Log.i("CRM", it.toString())
        }
        */

        return pointsDataGains.toTypedArray()
    }

    fun onModeChanged(){

    }
}