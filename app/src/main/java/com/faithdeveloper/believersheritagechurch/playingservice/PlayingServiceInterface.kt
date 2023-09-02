package com.faithdeveloper.believersheritagechurch.playingservice

import com.faithdeveloper.believersheritagechurch.data.playing.PlaybackState
import com.faithdeveloper.believersheritagechurch.data.playing.PlayingSpeed

interface PlayingServiceInterface {
    fun playbackState(playbackState: PlaybackState)
    fun navigateBackwards()
    fun playingSpeed(playingSpeed: PlayingSpeed)
}