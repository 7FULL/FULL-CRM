package com.full.crm

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.VisualTransformation.Companion.None
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.full.crm.login.ui.Login
import com.full.crm.login.ui.LoginViewModel
import com.full.crm.network.API
import com.full.crm.ui.theme.CRMTheme
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarEvents
import com.himanshoe.kalendar.KalendarType
import com.himanshoe.kalendar.color.KalendarColors
import com.himanshoe.kalendar.ui.component.day.KalendarDayKonfig
import com.himanshoe.kalendar.ui.firey.DaySelectionMode
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate


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
fun Agenda() {
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Kalendar(
            currentDay = LocalDate(2023, 11, 3),
            kalendarType = KalendarType.Oceanic,
            modifier = Modifier.padding(16.dp),
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
            },
            onRangeSelected = { selectedRange, events ->
                // Handle range selection event
            },
            onErrorRangeSelected = { error ->
                // Handle error
            })
    }
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun PruebaPreview() {
    MaterialTheme {
        //Agenda()
        Login(loginViewModel = LoginViewModel())
    }
}