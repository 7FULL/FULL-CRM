package com.full.crm

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
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
import androidx.compose.material.icons.rounded.AssuredWorkload
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.full.crm.models.Role
import com.full.crm.navigation.NavigationManager
import com.full.crm.network.API
import com.full.crm.ui.theme.CRMTheme
import com.full.crm.utils.CustomFile
import com.full.crm.utils.FileDownloadWorker
import com.full.crm.utils.FileParams
import kotlinx.datetime.LocalDate


class MainActivity : ComponentActivity() {

    private lateinit var requestMultiplePermission: ActivityResultLauncher<Array<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        API.mainActivity = this

        requestMultiplePermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ){
            var isGranted = false
            it.forEach { s, b ->
                isGranted = b

                if (!isGranted){
                    //Toast.makeText(this, "Permiso: ${s} no concedido", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setContent {
            CRMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    requestMultiplePermission.launch(
                        arrayOf(
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.INTERNET,
                            android.Manifest.permission.POST_NOTIFICATIONS,
                        )
                    )

                    NavigationManager.InitializeNavigator()

                    //Kalendar(currentDay = LocalDate(1,1,1), kalendarType = KalendarType.Oceanic)
                }
            }
        }
    }

    fun startDownloadingFile(
        file: CustomFile,
        success:(String) -> Unit,
        failed:(String) -> Unit,
        running:() -> Unit,
        workManager: WorkManager
    ) {
        val data = Data.Builder()

        data.apply {
            putString(FileParams.KEY_FILE_NAME, file.name)
            putString(FileParams.KEY_FILE_URL, file.url.toString())
            putString(FileParams.KEY_FILE_TYPE, file.type)
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val fileDownloadWorker = OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        workManager.enqueueUniqueWork(
            "oneFileDownloadWork_${System.currentTimeMillis()}",
            ExistingWorkPolicy.KEEP,
            fileDownloadWorker
        )

        workManager.getWorkInfoByIdLiveData(fileDownloadWorker.id)
            .observe(this){ info->
                info?.let {
                    when (it.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            success(it.outputData.getString(FileParams.KEY_FILE_URI) ?: "")
                        }
                        WorkInfo.State.FAILED -> {
                            failed("Downloading failed!")
                        }
                        WorkInfo.State.RUNNING -> {
                            running()
                        }
                        else -> {
                            failed("Something went wrong")
                        }
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

            if (API.isAdministrator) {
                Column (
                    modifier = modifier
                        .weight(1f)
                        .fillMaxWidth()
                ){
                    Icon(
                        tint = if (selectedIcon == 4) Color(R.color.primaryDescendant) else Color.White,
                        imageVector = Icons.Rounded.AssuredWorkload,
                        contentDescription = "Vector",
                        modifier = Modifier
                            .size(size = 50.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                            .clickable {
                                if (selectedIcon != 4) NavigationManager.instance?.navigate(
                                    "administration"
                                )
                            }
                    )
                }

            }


            /*
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
            */

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

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(orientation) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Preview()
@Composable
private fun PruebaPreview() {
    MaterialTheme {
        //Agenda()
        //Login(loginViewModel = LoginViewModel())
    }
}