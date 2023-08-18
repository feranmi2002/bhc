package com.faithdeveloper.believersheritagechurch.data.playing

interface RepositoryServiceInterface {
    fun playbackState(playbackState: PlaybackState)
    fun unbind()

}