package com.full.crm.ui.theme.agenda

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.full.crm.OptionsBar
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarEvents
import com.himanshoe.kalendar.KalendarType
import com.himanshoe.kalendar.color.KalendarColors
import com.himanshoe.kalendar.ui.component.day.KalendarDayKonfig
import com.himanshoe.kalendar.ui.firey.DaySelectionMode
import kotlinx.datetime.LocalDate
import java.util.Calendar


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Agenda(modifier: Modifier = Modifier, agendyViewModel: AgendyViewModel) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1 // Suma 1 porque en Calendar, enero es 0.
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    Scaffold(bottomBar = { OptionsBar(modifier = Modifier.padding(top = 5.dp),selectedIcon = 0) }) {
        Column {
            Row {
                /*Kalendar(
                    modifier = modifier,
                    currentDay = LocalDate(year, month, day),
                    kalendarType = KalendarType.Oceanic,
                    showLabel = true,
                    events = KalendarEvents(),
                    kalendarHeaderTextKonfig = null,
                    kalendarColors = KalendarColors.default(),
                    kalendarDayKonfig = KalendarDayKonfig.default(),
                    daySelectionMode = DaySelectionMode.Single,
                    dayContent = null,
                    headerContent = null,
                    onDayClick = { selectedDay, events ->
                        // Handle day click event
                    }
                )*/
            }
            Row(modifier = Modifier.padding(bottom = 75.dp)) {
                LazyColumn(
                    content =
                    {
                        items(100) {
                            Text(text = "Item $it")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}