package com.faithdeveloper.believersheritagechurch.ui.playing

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.faithdeveloper.believersheritagechurch.R
import com.faithdeveloper.believersheritagechurch.data.Util
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.playing.PlaybackState
import com.faithdeveloper.believersheritagechurch.data.playing.PlayingSpeed
import com.faithdeveloper.believersheritagechurch.download.DownloadStatus
import com.faithdeveloper.believersheritagechurch.viewmodel.PlayingViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlayingScreen(
    onClickBack: () -> Unit,
    playbackStateAction: () -> Unit,
    onSliderInteraction: (length: Float) -> Unit,
    playingViewModel: PlayingViewModel,
    onDownload: () -> Unit,
    playingScreenLeft: () -> Unit,
    pauseDueToSlider: () -> Unit,
    stopMedia: () -> Unit,
    speedPlay: () -> Unit
) {

    DisposableEffect(key1 = playingViewModel) {
        onDispose {
            playingScreenLeft.invoke()
        }
    }

    val message = playingViewModel.message

    val playbackState by playingViewModel.playbackState.observeAsState(initial = PlaybackState.BUFFERING)

    val playbackPosition: Int by playingViewModel.playbackPosition.collectAsStateWithLifecycle(
        initialValue = 0
    )

    val downloadStatus by playingViewModel.downloadStatus.observeAsState(initial = DownloadStatus.UNDOWNLOADED)

    val navigateBackwards by playingViewModel.navigateBackwards.observeAsState(false)

    val playingSpeed by playingViewModel.playingSpeed.observeAsState()

    val infiniteTransition = rememberInfiniteTransition()

    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    if (navigateBackwards) {
        onClickBack.invoke()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Top(message.title)

        GlideImage(
            contentScale = ContentScale.FillBounds,
            alignment = Alignment.Center,
            model = message.imageLink,
            modifier = if (playbackState == PlaybackState.PLAYING) {
                Modifier
                    .weight(0.5f)
                    .clip(CircleShape)
                    .size(250.dp)
                    .rotate(angle)
            } else {
                Modifier
                    .weight(0.5f)
                    .clip(CircleShape)
                    .size(250.dp)
            },
            contentDescription = null
        )

        MessageDescription(
            preacher = message.preacher,
            description = message.description
        )

        TimeCount(
            length = message.length,
            playbackState = playbackState,
            playbackPosition = playbackPosition.toFloat(),
            onSliderInteraction = onSliderInteraction,
            pauseDueToSlider = pauseDueToSlider
        )

        PlayingControls(
            playbackStateAction = playbackStateAction,
            onDownload = onDownload,
            message = message,
            playbackState = playbackState,
            downloadStatus = downloadStatus,
            stopMedia = stopMedia,
            speedPlay = speedPlay,
            playingSpeed = playingSpeed!!
        )
    }
}

@Composable
fun Top(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun MessageDescription(preacher: String, description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 12.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = description,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = preacher,
            style = MaterialTheme.typography.bodyMedium
        )

    }
}

@Composable
fun TimeCount(
    length: Long,
    playbackPosition: Float,
    onSliderInteraction: (length: Float) -> Unit,
    playbackState: PlaybackState,
    pauseDueToSlider: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        var sliderPosition by remember {
            mutableStateOf(0F)
        }

        LaunchedEffect(key1 = playbackPosition) {
            while (playbackState == PlaybackState.PLAYING) {
                sliderPosition = playbackPosition
                delay(500)
            }
        }


        Text(
            text = if (sliderPosition == 0F) {
                "0:00:00"
            } else {
                Util.formatDuration(sliderPosition.toLong())
            },
            style = MaterialTheme.typography.bodyMedium
        )

        Slider(
            modifier = Modifier
                .padding(start = 3.dp, end = 3.dp)
                .weight(1f),
            value = sliderPosition,
            valueRange = 0F..length.toFloat(),
            onValueChangeFinished = {
                onSliderInteraction.invoke(sliderPosition)
            },
            onValueChange = {
                if (playbackState == PlaybackState.PLAYING || playbackState == PlaybackState.PAUSED) {
                    pauseDueToSlider.invoke()
                    sliderPosition = it
                }
            }
        )
        Text(
            text = Util.formatDuration(length),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun PlayingControls(
    playbackStateAction: () -> Unit,
    onDownload: () -> Unit,
    message: Message,
    playbackState: PlaybackState,
    downloadStatus: DownloadStatus,
    stopMedia: () -> Unit,
    speedPlay: () -> Unit,
    playingSpeed: PlayingSpeed
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        var showDialog by rememberSaveable {
            mutableStateOf(false)
        }

        if (showDialog) {
            InfoDialog(message = message, showDialog = {
                showDialog = it
            })
        }

        Image(
            modifier = Modifier.clickable {
                showDialog = !showDialog
            },
            painter = painterResource(id = R.drawable.ic_outline_info_24), contentDescription = null
        )

        if (playbackState == PlaybackState.PLAYING || playbackState == PlaybackState.PAUSED) {
            Text(
                modifier = Modifier.clickable {
                    speedPlay.invoke()
                },
                text = when (playingSpeed) {
                    PlayingSpeed.ONE_X -> "1x"
                    PlayingSpeed.ONE_FIVE_X -> "1.5x"
                    PlayingSpeed.TWO_X -> "2x"
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }

        when (playbackState) {
            PlaybackState.BUFFERING -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            PlaybackState.PLAYING -> {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            playbackStateAction.invoke()
                        },
                    painter = painterResource(
                        id = R.drawable.ic_round_pause_circle_24

                    ),
                    contentDescription = null
                )
            }

            PlaybackState.PAUSED -> {
                PlayButton(playbackStateAction = playbackStateAction)
            }

            PlaybackState.FAILED -> {
                PlayButton(playbackStateAction = playbackStateAction)
            }

            PlaybackState.FINISHED -> {
                PlayButton(playbackStateAction = playbackStateAction)
            }
        }


        when (downloadStatus) {
            DownloadStatus.DOWNLOADING -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(enabled = false) {},
                    color = Color.Black
                )
            }

            else -> {
                Image(
                    modifier = Modifier.clickable {
                        if (downloadStatus != DownloadStatus.SUCCESSFUL) {
                            onDownload.invoke()
                        }
                    },
                    painter = when (downloadStatus) {
                        DownloadStatus.UNDOWNLOADED -> {
                            painterResource(id = R.drawable.ic_round_download_24)
                        }

                        DownloadStatus.FAILED -> {
                            painterResource(id = R.drawable.ic_round_download_24)
                        }
                        DownloadStatus.SUCCESSFUL -> {
                            painterResource(id = R.drawable.ic_round_download_done_24)
                        }
                        else -> {
//                            this will never execute
                            painterResource(id = 0)
                        }
                    },
                    contentDescription = null
                )
            }
        }

        if (playbackState == PlaybackState.PLAYING || playbackState == PlaybackState.PAUSED) {
            Image(
                modifier = Modifier.clickable {
                    stopMedia.invoke()
                },
                painter = painterResource(id = R.drawable.ic_baseline_stop_24),
                contentDescription = "Stop"
            )
        }
    }
}

@Composable
fun PlayButton(playbackStateAction: () -> Unit) {
    Image(
        modifier = Modifier
            .size(50.dp)
            .clickable {
                playbackStateAction.invoke()
            },
        painter = painterResource(
            id = R.drawable.ic_round_play_circle_24
        ),
        contentDescription = null
    )
}

@Composable
fun InfoDialog(message: Message, showDialog: (Boolean) -> Unit) {
    Dialog(
        onDismissRequest = {
            showDialog.invoke(false)
        },
        properties = DialogProperties()
    ) {
        Surface(
            modifier = Modifier.wrapContentSize(align = Alignment.Center),
            shape = RoundedCornerShape(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .wrapContentSize(align = Alignment.Center)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    DialogInfoItem(title = "Title", item = message.title)
                }

                item {
                    DialogInfoItem(title = "Preacher", item = message.preacher)
                }

                item {
                    DialogInfoItem(title = "Venue", item = message.venue)
                }

                item {
                    DialogInfoItem(title = "Date", item = Util.formatDate(message.date))
                }

                item {
                    DialogInfoItem(title = "Length", item = Util.formatDuration(message.length))
                }

                item {
                    DialogInfoItem(title = "Description", item = message.description)
                }
            }
        }
    }
}

@Composable
fun DialogInfoItem(title: String, item: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 16.sp
        )

        Text(
            text = item,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp
        )
    }
}
