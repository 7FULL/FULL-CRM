package com.full.crm.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.full.crm.MainActivity
import com.full.crm.R
import java.io.File
import java.io.FileOutputStream
import java.net.URL


class FileDownloadWorker(
    private val context: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val fileUrl = inputData.getString(FileParams.KEY_FILE_URL) ?: ""
        val fileName = inputData.getString(FileParams.KEY_FILE_NAME) ?: ""
        val fileType = inputData.getString(FileParams.KEY_FILE_TYPE) ?: ""

        if (fileName.isEmpty()
            || fileType.isEmpty()
            || fileUrl.isEmpty()
        ){
            Result.failure()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val name = NotificationConstants.CHANNEL_NAME
            val description = NotificationConstants.CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NotificationConstants.CHANNEL_ID,name,importance)
            channel.description = description

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)

        }

        val builder = NotificationCompat.Builder(context,NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Descargando archivo...")
            .setOngoing(true)
            .setProgress(0,0,true)

        if (ActivityCompat.checkSelfPermission(
                 context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as MainActivity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )

            return Result.failure()
        }
        NotificationManagerCompat.from(context).notify(NotificationConstants.NOTIFICATION_ID,builder.build())

        val uri = getSavedFileUri(
            fileName = fileName,
            fileType = fileType,
            fileUrl = fileUrl,
            context = context
        )

        NotificationManagerCompat.from(context).cancel(NotificationConstants.NOTIFICATION_ID)
        return if (uri != null){
            Result.success(workDataOf(FileParams.KEY_FILE_URI to uri.toString()))
        }else{
            Result.failure()
        }
    }
}

object FileParams{
    const val KEY_FILE_URL = "key_file_url"
    const val KEY_FILE_TYPE = "key_file_type"
    const val KEY_FILE_NAME = "key_file_name"
    const val KEY_FILE_URI = "key_file_uri"
}

object NotificationConstants{
    const val CHANNEL_NAME = "download_file_worker_demo_channel"
    const val CHANNEL_DESCRIPTION = "download_file_worker_demo_description"
    const val CHANNEL_ID = "download_file_worker_demo_channel_123456"
    const val NOTIFICATION_ID = 1
}

private fun getSavedFileUri(
    fileName:String,
    fileType:String,
    fileUrl:String,
    context: Context): Uri?{
    val mimeType = when(fileType){
        "PDF" -> "application/pdf"
        "DOCX" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        "CSV" -> "text/csv"
        "JSON" -> "application/json"
        "XML" -> "application/xml"
        "TXT" -> "text/plain"
        else -> ""
    }

    if (mimeType.isEmpty()) return null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/CRM")
        }

        val resolver = context.contentResolver

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        return if (uri!=null){
            URL(fileUrl).openStream().use { input->
                resolver.openOutputStream(uri).use { output->
                    input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                }
            }
            uri
        }else{
            null
        }

    }else{
        val target = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            fileName
        )
        URL(fileUrl).openStream().use { input->
            FileOutputStream(target).use { output ->
                input.copyTo(output)
            }
        }

        return target.toUri()
    }
}

data class CustomFile(
    val id:String,
    val name:String,
    val type:String,
    val url:Uri,
    var downloadedUri:String?=null,
    var isDownloading:Boolean = false,
)

@Composable
fun ItemFile(
    file: CustomFile,
    startDownload:(CustomFile) -> Unit,
    openFile:(CustomFile) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = Color(0xFF077FD5), shape = RoundedCornerShape(16.dp))
            .clickable {
                if (!file.isDownloading){
                    if (file.downloadedUri.isNullOrEmpty()){
                        startDownload(file)
                    }else{
                        openFile(file)
                    }
                }
            }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            ) {
                Text(
                    text = file.name,
                    color = Color.Black
                )

                Row {
                    val description = if (file.isDownloading){
                        "Descargando..."
                    }else{
                        if (file.downloadedUri.isNullOrEmpty()) "Descargar archivo" else "Abrir archivo"
                    }
                    Text(
                        text = description,
                        color = Color.DarkGray
                    )
                }

            }

            if (file.isDownloading){
                CircularProgressIndicator(
                    color = Color.Blue,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterVertically)
                )
            }

        }

    }
}
