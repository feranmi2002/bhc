package com.faithdeveloper.believersheritagechurch.data.playing

interface MainActivityPlayingServiceInterface {
    fun playbackState(playbackState: PlaybackState)
    fun mediaStarted(state:Boolean)

}