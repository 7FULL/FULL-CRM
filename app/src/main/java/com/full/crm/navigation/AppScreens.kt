package com.full.crm.navigation

sealed class AppScreens(val route: String) {
    object Login : AppScreens("login")
    object Agenda : AppScreens("agenda")
    object Bills : AppScreens("bills")
    object ForgotPassword : AppScreens("forgotPassword")
    object CheckToken : AppScreens("checkToken")
    object NewPassword : AppScreens("newPassword")
    object Analisis : AppScreens("analisis")

    object AnalisisFullScreen : AppScreens("analisisFullScreen")
}
