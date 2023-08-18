package com.faithdeveloper.believersheritagechurch.data.playing

import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.playingservice.PlayingServiceInterface

interface PlayingRepository {
    fun startService(message: Message)
    fun unbindService()
    fun playbackPosition(): Int
    fun pauseMedia()
    fun resumeMedia()
    fun seekTo(position:Float)
    fun viewModelInstance(viewModel:PlayingServiceInterface)
    fun restartMediaAfterError()
    fun restartMediaAfterCompletion()
    fun endService()
    fun pauseDueToSlider()
    fun setPlayingSpeed(playingSpeed: PlayingSpeed)

}