package com.faithdeveloper.believersheritagechurch.download

import android.app.DownloadManager
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.download.DownloadBroadcastReceiver.Companion.DOWNLOAD_ID
import com.faithdeveloper.believersheritagechurch.download.DownloadBroadcastReceiver.Companion.DOWNLOAD_INTENT_TYPE
import kotlin.properties.Delegates

class DownloadRepositoryImpl(private val context: Context) :
    DownloadRepository {
    private lateinit var playingViewModel: DownloadInterface
    private var downloadID by Delegates.notNull<Long>()
    private val downloadBroadcastReceiver: DownloadBroadcastReceiver = DownloadBroadcastReceiver()
    private val innerDownloadBroadcastReceiver = InnerDownloadBroadcastReceiver()
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun download(message: Message) {
        val request = DownloadManager.Request(message.audioLink.toUri())
            .setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "B.H.C/${message.type}/${message.title}.${message.audioType}"
            )
            .setMimeType("audio")
            .setTitle("${message.title}.${message.audioType}")
        request.allowScanningByMediaScanner()

        downloadID = downloadManager.enqueue(request)

    }

    override fun instance(playingViewModel: DownloadInterface) {
        this.playingViewModel = playingViewModel
        context.applicationContext.registerReceiver(downloadBroadcastReceiver, IntentFilter())
        ContextCompat.registerReceiver(
            context.applicationContext,
            innerDownloadBroadcastReceiver,
            IntentFilter.create(context.applicationContext.packageName, DOWNLOAD_INTENT_TYPE),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun unregisterReceiver() {
        try {
            context.applicationContext.unregisterReceiver(downloadBroadcastReceiver)
            context.applicationContext.unregisterReceiver(innerDownloadBroadcastReceiver)
        }catch (e:Exception){
//             do nothing
        }

    }

    inner class InnerDownloadBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DOWNLOAD_ID, -1L)
            if (id != -1L) {
                if (id == downloadID) {
                    val query = DownloadManager.Query().setFilterById(downloadID)
                    val cursor = downloadManager.query(query)
                    if (cursor.moveToFirst()) {
                        val status =
                            cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            playingViewModel.downloadStatus(DownloadStatus.SUCCESSFUL)
                        } else {
                            playingViewModel.downloadStatus(DownloadStatus.FAILED)
                        }
                    } else {
                        playingViewModel.downloadStatus(DownloadStatus.FAILED)
                    }
                }
            }
        }
    }
}
