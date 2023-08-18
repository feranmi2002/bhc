package com.faithdeveloper.believersheritagechurch.playingservice

import com.faithdeveloper.believersheritagechurch.data.playing.PlaybackState

interface PlayingServiceInterface {
    fun playbackState(playbackState: PlaybackState)
    fun navigateBackwards()
}