package com.faithdeveloper.believersheritagechurch.ui.playing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.faithdeveloper.believersheritagechurch.viewmodel.PlayingViewModel

@Composable
fun PlayingRoute(playingViewModel: PlayingViewModel, onClickBack: () -> Unit) {
    playingViewModel.startMedia()

    PlayingScreen(
        onClickBack = onClickBack,
        playbackStateAction =
        {
            playingViewModel.playbackStateAction()
        },
        playingViewModel = playingViewModel,
        onSliderInteraction = { position ->
            playingViewModel.seekTo(position)
        },
        onDownload = {
            playingViewModel.download()
        },
        playingScreenLeft = {
            playingViewModel.playingScreenLeft()
        },
        pauseDueToSlider = {
            playingViewModel.pauseDueToSlider()
        },
        stopMedia = {
            playingViewModel.stopMedia()
            onClickBack.invoke()
        }
    )
}