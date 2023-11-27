package com.full.crm.navigation

import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.full.crm.LockScreenOrientation
import com.full.crm.ui.theme.agenda.Agenda
import com.full.crm.ui.theme.agenda.AgendyViewModel
import com.full.crm.ui.theme.agenda.Analisis
import com.full.crm.ui.theme.agenda.AnalisisViewModel
import com.full.crm.ui.theme.analisis.fullscreen.AnalisisFullScreen
import com.full.crm.ui.theme.analisis.fullscreen.AnalisisFullScreenViewModel
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
            composable(AppScreens.Login.route) {
                Login(loginViewModel = LoginViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.Agenda.route) {
                Agenda(agendyViewModel = AgendyViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.Bills.route) {
                Bills(billsViewModel = BillsViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.ForgotPassword.route) {
                ForgotPassword(forgotPasswordViewModel = ForgotPasswordViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.CheckToken.route) {
                CheckToken(checkTokenViewModel = CheckTokenViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.NewPassword.route) {
                NewPassword(newPasswordViewModel = NewPasswordViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.Analisis.route) {
                Analisis(analisisViewModel = AnalisisViewModel())
                //LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
            composable(AppScreens.AnalisisFullScreen.route) {
                AnalisisFullScreen(analisisFullScreenViewModel = AnalisisFullScreenViewModel())
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                //LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            }
        }
    }
}