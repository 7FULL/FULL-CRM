package com.full.crm.ui.theme.agenda

import android.net.Uri
import android.util.Half.toFloat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import co.yml.charts.common.model.Point
import com.full.crm.models.Appointment
import com.full.crm.models.Bill
import com.full.crm.network.API
import com.full.crm.utils.CustomFile
import com.full.crm.utils.FileDownloadWorker
import com.full.crm.utils.FileParams
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
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

    private val _dataPoints = MutableLiveData<Array<Point>>(initialize())
    val dataPoints: LiveData<Array<Point>> = _dataPoints

    fun initialize(): Array<Point>{
        val calendar = Calendar.getInstance()

        val endDate = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_YEAR, - 31)

        val startDate = calendar.timeInMillis

        return updateDataPoints(startDate, endDate)
    }

    fun updateDataPoints(startDate: Long, endDate: Long): Array<Point> {
        //Limpiamos los puntos de la grafica
        pointsDataPayments.clear()
        pointsDataLosses.clear()
        pointsDataGains.clear()
        _last7Days.value!!.clear()

        //Añadimos el punto de inicio
        pointsDataPayments.add(0, Point(0f, 0f))
        pointsDataGains.add(0, Point(0f, 0f))
        pointsDataLosses.add(0, Point(0f, 0f))

        _last7Days.value!!.add(0, Date())

        val startCalendar = Calendar.getInstance()
        startCalendar.time = Date(startDate)

        val endCalendar = Calendar.getInstance()
        endCalendar.time = Date(endDate)

        //Obtenemos los ultimos 7 dias en los que se haya emitido una factura
        bills!!.forEach {
            val date = it.getEmisionDate()!!
            val calendar = Calendar.getInstance()
            calendar.time = date

            val day = calendar.get(Calendar.DAY_OF_YEAR)

            if (day >= startCalendar.get(Calendar.DAY_OF_YEAR) && day <= endCalendar.get(Calendar.DAY_OF_YEAR)) {
                _last7Days.value!!.add(date)

                val bill = it

                if (bill.isPaid()) {
                    pointsDataPayments.add(Point(pointsDataPayments.size.toFloat(), bill.getPrice()!!.toFloat()))
                    pointsDataGains.add(Point(pointsDataGains.size.toFloat(), bill.getPrice()!!.toFloat()))
                }

                if (!bill.isPaid()){
                    pointsDataLosses.add(Point(pointsDataLosses.size.toFloat(), bill.getPrice()!!.toFloat()))
                    pointsDataGains.add(Point(pointsDataGains.size.toFloat(), -bill.getPrice()!!.toFloat()))
                }
            }
        }

        try {
            _dataPoints.value = pointsDataGains.toTypedArray()
        }catch (NullPointerException: NullPointerException){
            // Hacemos este trycatch porque al llamar desde la inicializacion del live data,
            // el valor de pointsDataGains es null sin embargo al llamar a la funcion
        }

        return pointsDataGains.toTypedArray()
    }

    fun export(value: String): Uri? {
        var file: Uri? = null

        val bills = API.User.value!!.getBills()

        when (value) {
            "XML" -> {
                //Creamos el documento XML
                var xml = ""

                xml += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                xml += "<bills>\n"

                bills!!.forEach {
                    xml += "\t<bill>\n"
                    xml += "\t\t<id>${it.getId()}</id>\n"
                    xml += "\t\t<clientId>${it.getClientID()}</clientId>\n"
                    xml += "\t\t<employeeId>${it.getEmployeeID()}</employeeId>\n"
                    xml += "\t\t<price>${it.getPrice()}</price>\n"
                    xml += "\t\t<emisionDate>${it.getEmisionDate()}</emisionDate>\n"
                    xml += "\t\t<expirationDate>${it.getExpirationDate()}</expirationDate>\n"
                    xml += "\t\t<isPaid>${it.isPaid()}</isPaid>\n"
                    xml += "\t</bill>\n"
                }

                xml += "</bills>\n"

                //Creamos el archivo XML
                val aux = File.createTempFile("bills", ".xml")

                aux.writeText(xml)

                file = Uri.fromFile(aux)
            }
            "JSON" -> {
                val aux = File.createTempFile("bills", ".json")

                aux.writeText(Gson().toJson(bills))

                file = Uri.fromFile(aux)
            }
            "DOCX" -> {
                val aux = File.createTempFile("bills", ".docx")

                aux.writeText("ID\tCLIENT ID\tEMPLOYEE ID\tPRICE\tEMISION DATE\tEXPIRATION DATE\tIS PAID\n")

                bills!!.forEach {
                    aux.appendText("${it.getId()}\t${it.getClientID()}\t${it.getEmployeeID()}\t${it.getPrice()}\t${it.getEmisionDate()}\t${it.getExpirationDate()}\t${it.isPaid()}\n")
                }

                file = Uri.fromFile(aux)
            }
            "PDF" -> {
                val aux = File.createTempFile("bills", ".pdf")

                aux.writeText("ID\tCLIENT ID\tEMPLOYEE ID\tPRICE\tEMISION DATE\tEXPIRATION DATE\tIS PAID\n")

                bills!!.forEach {
                    aux.appendText("${it.getId()}\t${it.getClientID()}\t${it.getEmployeeID()}\t${it.getPrice()}\t${it.getEmisionDate()}\t${it.getExpirationDate()}\t${it.isPaid()}\n")
                }

                file = Uri.fromFile(aux)
            }
            "CSV" -> {
                val aux = File.createTempFile("bills", ".csv")

                aux.writeText("ID,CLIENT ID,EMPLOYEE ID,PRICE,EMISION DATE,EXPIRATION DATE,IS PAID\n")

                bills!!.forEach {
                    aux.appendText("${it.getId()},${it.getClientID()},${it.getEmployeeID()},${it.getPrice()},${it.getEmisionDate()},${it.getExpirationDate()},${it.isPaid()}\n")
                }

                file = Uri.fromFile(aux)
            }
            "TXT" -> {
                val aux = File.createTempFile("bills", ".txt")

                aux.writeText("ID\tCLIENT ID\tEMPLOYEE ID\tPRICE\tEMISION DATE\tEXPIRATION DATE\tIS PAID\n")

                bills!!.forEach {
                    aux.appendText("${it.getId()}\t${it.getClientID()}\t${it.getEmployeeID()}\t${it.getPrice()}\t${it.getEmisionDate()}\t${it.getExpirationDate()}\t${it.isPaid()}\n")
                }

                file = Uri.fromFile(aux)
            }
        }

        return file
    }
}