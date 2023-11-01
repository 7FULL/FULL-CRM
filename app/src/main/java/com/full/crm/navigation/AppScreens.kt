package com.full.crm.navigation

sealed class AppScreens(val route: String) {
    object Login : AppScreens("login")
    object Agenda : AppScreens("agenda")
}
