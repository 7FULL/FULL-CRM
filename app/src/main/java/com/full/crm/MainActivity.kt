package com.full.crm

import android.app.Activity
import android.content.Context
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import com.full.crm.navigation.NavigationManager
import com.full.crm.network.API
import com.full.crm.ui.theme.CRMTheme
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarType
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
                    NavigationManager.InitializeNavigator()

                    //Kalendar(currentDay = LocalDate(1,1,1), kalendarType = KalendarType.Oceanic)
                }
            }
        }
    }
}


@Composable
fun OptionsBar(modifier: Modifier = Modifier, selectedIcon: Int) {
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
                Icon(
                    tint = if (selectedIcon == 0) Color(R.color.primaryDescendant) else Color.White,
                    imageVector = Icons.Rounded.DateRange,
                    contentDescription = "Vector",
                    modifier = Modifier
                        .size(size = 50.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable { if (selectedIcon != 0) NavigationManager.instance?.navigate("agenda") }
                )
            }
            Column (
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
            ){
                Icon(
                    tint = if (selectedIcon == 1) Color(R.color.primaryDescendant) else Color.White,
                    imageVector = Icons.Rounded.AccountBalanceWallet,
                    contentDescription = "Vector",
                    modifier = Modifier
                        .size(size = 50.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable { if (selectedIcon != 1) NavigationManager.instance?.navigate("bills") }
                )
            }
            Column (
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
            ){
                Icon(
                    tint = if (selectedIcon == 2) Color(R.color.primaryDescendant) else Color.White,
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Vector",
                    modifier = Modifier
                        .size(size = 50.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable { if (selectedIcon != 2) NavigationManager.instance?.navigate("analisis") }
                )
            }
            Column (
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
            ){
                Icon(
                    tint = if (selectedIcon == 3) Color(R.color.primaryDescendant) else Color.White,
                    imageVector = Icons.Rounded.ExitToApp,
                    contentDescription = "Vector",
                    modifier = Modifier
                        .size(size = 50.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable { if (selectedIcon != 3) API.logout() }
                )
            }
        }
    }
}

@Preview()
@Composable
private fun PruebaPreview() {
    MaterialTheme {
        //Agenda()
        //Login(loginViewModel = LoginViewModel())
    }
}