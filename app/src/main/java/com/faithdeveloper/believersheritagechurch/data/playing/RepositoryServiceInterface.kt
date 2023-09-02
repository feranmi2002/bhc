package com.faithdeveloper.believersheritagechurch.data.playing

interface RepositoryServiceInterface {
    fun playbackState(playbackState: PlaybackState)
    fun unbindService()
    fun playingSpeed(playingSpeed: PlayingSpeed)

}