package com.faithdeveloper.believersheritagechurch.viewmodel

import androidx.lifecycle.*
import com.faithdeveloper.believersheritagechurch.data.PlaybackState
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.playing.PlayingRepository
import com.faithdeveloper.believersheritagechurch.download.DownloadInterface
import com.faithdeveloper.believersheritagechurch.download.DownloadRepository
import com.faithdeveloper.believersheritagechurch.download.DownloadStatus
import com.faithdeveloper.believersheritagechurch.playingservice.PlayingServiceInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class PlayingViewModel private constructor(
    private val playingRepository: PlayingRepository,
    val message: Message,
    private val downloadRepository: DownloadRepository
) : ViewModel(), PlayingServiceInterface, DownloadInterface {
    private val refreshInterval = 500L
    private var stopMedia = false

    init {
        playingRepository.viewModelInstance(this)
        downloadRepository.instance(this)
    }

    private val mplaybackState: MutableLiveData<PlaybackState> = MutableLiveData<PlaybackState>()
    val playbackState: LiveData<PlaybackState> get() = mplaybackState

    private val mdownloadStatus: MutableLiveData<DownloadStatus> =
        MutableLiveData(DownloadStatus.UNDOWNLOADED)
    val downloadStatus: LiveData<DownloadStatus> get() = mdownloadStatus

    private val _navigateBackwards = MutableLiveData(false)
    val navigateBackwards:LiveData<Boolean> get() = _navigateBackwards


    var playbackPosition = flow {
        while (true) {
            emit(playingRepository.playbackPosition())
            delay(refreshInterval)
        }
    }

    fun playbackStateAction() =
        when (playbackState.value) {
            PlaybackState.PLAYING -> {
                pauseMedia()
            }

            PlaybackState.PAUSED -> {
                resumeMedia()
            }

            PlaybackState.BUFFERING -> {
//                   do nothing
            }

            PlaybackState.FAILED -> {
                restartAfterMediaFail()
            }

            PlaybackState.FINISHED -> {
                restartAfterPlaybackFinish()
            }

            else -> {

            }
        }


    fun startMedia() {
        playingRepository.startService(message)
    }

    private fun pauseMedia() {
        playingRepository.pauseMedia()
    }

    private fun resumeMedia() {
        playingRepository.resumeMedia()
    }

    fun download() {
        when (downloadStatus.value) {
            DownloadStatus.DOWNLOADING -> {
//                do nothing
            }
            DownloadStatus.UNDOWNLOADED -> {
                downloadRepository.download(message)
                mdownloadStatus.value = DownloadStatus.DOWNLOADING
            }
            DownloadStatus.SUCCESSFUL -> {
//             do nothing
            }
            DownloadStatus.FAILED -> {
                downloadRepository.download(message)
                mdownloadStatus.value = DownloadStatus.DOWNLOADING
            }
            else -> {
//             do nothing
            }
        }
    }

    override fun downloadStatus(downloadStatus: DownloadStatus) {
        mdownloadStatus.value = downloadStatus
    }

    override fun onCleared() {
        if (!stopMedia) {
            playingRepository.unbindService()
        }
        downloadRepository.unregisterReceiver()
        super.onCleared()
    }

    fun seekTo(position: Float) {
        playingRepository.seekTo(position)
    }

    private fun restartAfterMediaFail() {
        playingRepository.restartMediaAfterError()
    }

    private fun restartAfterPlaybackFinish() {
        playingRepository.restartMediaAfterCompletion()
    }

    override fun playbackState(playbackState: PlaybackState) {
        mplaybackState.value = playbackState
    }

    fun playingScreenLeft() {
        if (playbackState.value == PlaybackState.FINISHED || playbackState.value == PlaybackState.FAILED) {
            playingRepository.endService()
        }
    }

    override fun navigateBackwards() {
        stopMedia = true
        _navigateBackwards.value = true
    }

    fun pauseDueToSlider() {
        playingRepository.pauseDueToSlider()
    }

    fun stopMedia() {
        stopMedia = true
        playingRepository.endService()
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            repository: PlayingRepository,
            message: Message,
            downloadRepository: DownloadRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayingViewModel(repository, message, downloadRepository) as T
                }
            }
    }


}