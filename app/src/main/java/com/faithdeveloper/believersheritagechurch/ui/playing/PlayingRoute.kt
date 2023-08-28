package com.faithdeveloper.believersheritagechurch.ui.playing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.viewmodel.PlayingViewModel

@Composable
fun PlayingRoute(
    playingViewModel: PlayingViewModel,
    onClickBack: () -> Unit,
    mainActivity: MainActivity
) {
    LaunchedEffect(key1 = playingViewModel) {
        if (mainActivity.playingRepository == null) {
            playingViewModel.startMedia()
            mainActivity.playingRepositorySet(playingRepository = playingViewModel.playingRepository)
        }else {
            playingViewModel.pollDataFromAlreadySetPlayingRepository()
        }
    }

    PlayingScreen(
        onClickBack = onClickBack,
        playbackStateAction =
        {
            playingViewModel.playbackStateAction()
        },
        onSliderInteraction = { position ->
            playingViewModel.seekTo(position)
        },
        playingViewModel = playingViewModel,
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
    ) {
        playingViewModel.speedPlay()
    }
}