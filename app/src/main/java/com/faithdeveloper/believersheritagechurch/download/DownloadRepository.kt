package com.faithdeveloper.believersheritagechurch.download

import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.viewmodel.PlayingViewModel

interface DownloadRepository {
    fun download(message: Message)
    fun instance(playingViewModel: DownloadInterface)
    fun unregisterReceiver()
}