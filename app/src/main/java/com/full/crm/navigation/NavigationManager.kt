package com.full.crm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.full.crm.ui.theme.agenda.Agenda
import com.full.crm.ui.theme.agenda.AgendyViewModel
import com.full.crm.ui.theme.agenda.Analisis
import com.full.crm.ui.theme.agenda.AnalisisViewModel
import com.full.crm.ui.theme.bills.Bills
import com.full.crm.ui.theme.bills.BillsViewModel
import com.full.crm.ui.theme.login.Login
import com.full.crm.ui.theme.login.LoginViewModel
import com.full.crm.ui.theme.login.checkToken.CheckToken
import com.full.crm.ui.theme.login.checkToken.CheckTokenViewModel
import com.full.crm.ui.theme.login.checkToken.NewPassword
import com.full.crm.ui.theme.login.checkToken.NewPasswordViewModel
import com.full.crm.ui.theme.login.forgotPassword.ForgotPassword
import com.full.crm.ui.theme.login.forgotPassword.ForgotPasswordViewModel

object NavigationManager{
    var instance: NavHostController? = null;
    @Composable
    fun InitializeNavigator() {

        if (instance == null) instance = rememberNavController()

        NavHost(navController = instance!!, startDestination = AppScreens.Login.route)
        {
            composable(AppScreens.Login.route) { Login(loginViewModel = LoginViewModel()) }
            composable(AppScreens.Agenda.route) { Agenda(agendyViewModel = AgendyViewModel()) }
            composable(AppScreens.Bills.route) { Bills(billsViewModel = BillsViewModel()) }
            composable(AppScreens.ForgotPassword.route) { ForgotPassword(forgotPasswordViewModel = ForgotPasswordViewModel()) }
            composable(AppScreens.CheckToken.route) { CheckToken(checkTokenViewModel = CheckTokenViewModel()) }
            composable(AppScreens.NewPassword.route) { NewPassword(newPasswordViewModel = NewPasswordViewModel()) }
            composable(AppScreens.Analisis.route) { Analisis(agendyViewModel = AnalisisViewModel()) }
        }
    }
}