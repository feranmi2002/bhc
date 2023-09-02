package com.faithdeveloper.believersheritagechurch.viewmodel

import androidx.lifecycle.*
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.playing.PlaybackState
import com.faithdeveloper.believersheritagechurch.data.playing.PlayingRepository
import com.faithdeveloper.believersheritagechurch.data.playing.PlayingSpeed
import com.faithdeveloper.believersheritagechurch.download.DownloadInterface
import com.faithdeveloper.believersheritagechurch.download.DownloadRepository
import com.faithdeveloper.believersheritagechurch.download.DownloadStatus
import com.faithdeveloper.believersheritagechurch.playingservice.PlayingServiceInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class PlayingViewModel private constructor(
    val playingRepository: PlayingRepository,
    val message: Message,
    private val downloadRepository: DownloadRepository
) : ViewModel(), PlayingServiceInterface, DownloadInterface {
    private val refreshInterval = 500L

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
    val navigateBackwards: LiveData<Boolean> get() = _navigateBackwards

    private val _playingSpeed = MutableLiveData(PlayingSpeed.ONE_X)
    val playingSpeed: LiveData<PlayingSpeed> get() = _playingSpeed


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
        _navigateBackwards.value = true
    }

    fun pauseDueToSlider() {
        playingRepository.pauseDueToSlider()
    }

    fun stopMedia() {
        playingRepository.endService()
    }

    fun speedPlay() {
        when (playingSpeed.value!!) {
            PlayingSpeed.ONE_X -> {
                _playingSpeed.value = PlayingSpeed.ONE_FIVE_X
            }

            PlayingSpeed.ONE_FIVE_X -> {
                _playingSpeed.value = PlayingSpeed.TWO_X
            }

            PlayingSpeed.TWO_X -> {
                _playingSpeed.value = PlayingSpeed.ONE_X
            }
        }
        playingRepository.setPlayingSpeed(playingSpeed.value!!)
    }

    fun pollDataFromAlreadySetPlayingRepository() {
        mplaybackState.value = playingRepository.getPlaybackState()
        _playingSpeed.value = playingRepository.getPlayingSpeed()
    }

    fun stopMediaToRestartAnother() {
        playingRepository.stopMediaToRestartAnother(message)
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