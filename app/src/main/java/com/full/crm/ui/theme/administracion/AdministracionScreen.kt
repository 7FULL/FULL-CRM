package com.full.crm.ui.theme.administracion

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.full.crm.OptionsBar

@Composable
fun Administracion(modifier: Modifier = Modifier, administracionViewModel: AdministracionViewModel) {
    AdministationBody(administracionViewModel = administracionViewModel)
}

@Composable
fun AdministationBody(administracionViewModel: AdministracionViewModel){
    Scaffold(
        bottomBar =
        {
            OptionsBar(modifier =
            Modifier.padding(top = 5.dp),
                selectedIcon = 4
            )
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)){}
    }
}