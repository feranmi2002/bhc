package com.faithdeveloper.believersheritagechurch.ui.playing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.faithdeveloper.believersheritagechurch.data.playing.PlayingRepository
import com.faithdeveloper.believersheritagechurch.viewmodel.PlayingViewModel

@Composable
fun PlayingRoute(
    playingViewModel: PlayingViewModel,
    onClickBack: () -> Unit,
    setMainActivityPlayingRepository: (playingRepository:PlayingRepository) -> Unit
) {
    LaunchedEffect(key1 = playingViewModel ) {
        playingViewModel.startMedia()
        setMainActivityPlayingRepository.invoke(playingViewModel.playingRepository)
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