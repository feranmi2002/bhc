package com.faithdeveloper.believersheritagechurch.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DownloadBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
            if (id != -1L) {
                    val mintent = Intent(context!!.packageName).apply {
                        type = DOWNLOAD_INTENT_TYPE
                        putExtra(DOWNLOAD_ID, id)
                    }
                    context.sendBroadcast(mintent)
            }
        }
    }

    companion object {
        const val DOWNLOAD_INTENT_TYPE = "text/plain"
        const val DOWNLOAD_ID = "download_id"
    }
}