package com.faithdeveloper.believersheritagechurch.data.playing

import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.playingservice.PlayingServiceInterface

interface PlayingRepository {
    fun startService(message: Message, fromMainActivity: Boolean = false)
    fun playbackPosition(): Int
    fun pauseMedia()
    fun resumeMedia()
    fun seekTo(position: Float)
    fun viewModelInstance(viewModel: PlayingServiceInterface)
    fun restartMediaAfterError()
    fun restartMediaAfterCompletion()
    fun endService()
    fun pauseDueToSlider()
    fun setPlayingSpeed(playingSpeed: PlayingSpeed)
    fun getMessage(): Message
    fun mainActivityInstance(mainActivity: MainActivityPlayingServiceInterface)
    fun getPlaybackState(): PlaybackState
    fun getPlayingSpeed(): PlayingSpeed
    fun stopMediaToRestartAnother(message: Message)
}