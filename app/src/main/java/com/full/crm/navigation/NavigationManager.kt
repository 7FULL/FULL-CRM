package com.full.crm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.full.crm.ui.theme.agenda.Agenda
import com.full.crm.ui.theme.agenda.AgendyViewModel
import com.full.crm.ui.theme.login.Login
import com.full.crm.ui.theme.login.LoginViewModel

object NavigationManager{
    var instance: NavHostController? = null;
    @Composable
    fun InitializeNavigator() {
        instance = rememberNavController()
        NavHost(navController = instance!!, startDestination = AppScreens.Agenda.route)
        {
            composable(AppScreens.Login.route) { Login(loginViewModel = LoginViewModel()) }
            composable(AppScreens.Agenda.route) { Agenda(agendyViewModel = AgendyViewModel()) }
        }
    }
}