package com.full.crm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.full.crm.ui.theme.CRMTheme
import com.full.crm.ui.theme.login.Login
import com.full.crm.ui.theme.login.LoginViewModel
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarEvents
import com.himanshoe.kalendar.KalendarType
import com.himanshoe.kalendar.color.KalendarColors
import com.himanshoe.kalendar.ui.component.day.KalendarDayKonfig
import com.himanshoe.kalendar.ui.firey.DaySelectionMode
import kotlinx.datetime.LocalDate
import java.util.Calendar
import java.util.Date


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CRMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Agenda()
                    Login(loginViewModel = LoginViewModel())
                }
            }
        }
    }
}

@Composable
fun Agenda(modifier: Modifier = Modifier) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1 // Suma 1 porque en Calendar, enero es 0.
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    Box {
        Column {
            Row {
                Kalendar(
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
                )
            }
            Row {
                LazyColumn(content = {
                    items(100) {
                        Text(text = "Item $it")
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                )
            }
            Row {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(height = 130.dp)
                ){
                    Toolbar(modifier = Modifier.padding(top = 5.dp))
                }
            }
        }

    }
}

@Composable
private fun Toolbar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(height = 75.dp)
                .background(color = Color(0xFF1976D2))
                .border(border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.51f))))
        Row(modifier.fillMaxWidth()){
            Column (
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Vector",
                    modifier = Modifier
                        .requiredWidth(width = 40.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }
            Column (
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Vector",
                    modifier = Modifier
                        .requiredWidth(width = 40.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }
            Column (
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Vector",
                    modifier = Modifier
                        .requiredWidth(width = 40.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }
            Column (
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Vector",
                    modifier = Modifier
                        .requiredWidth(width = 40.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview()
@Composable
private fun PruebaPreview() {
    MaterialTheme {
        Agenda()
        //Login(loginViewModel = LoginViewModel())
    }
}