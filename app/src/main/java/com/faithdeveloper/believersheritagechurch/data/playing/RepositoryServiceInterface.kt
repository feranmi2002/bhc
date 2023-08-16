package com.faithdeveloper.believersheritagechurch.data.playing

import com.faithdeveloper.believersheritagechurch.data.PlaybackState

interface RepositoryServiceInterface {
    fun playbackState(playbackState: PlaybackState)
    fun unbind()

}