package com.faithdeveloper.believersheritagechurch.ui.programmes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.playing.PlaybackState
import com.faithdeveloper.believersheritagechurch.data.programmes.Programme
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.ui.messages.PlayingBar
import com.faithdeveloper.believersheritagechurch.ui.messages_section.ReusableTop
import com.faithdeveloper.believersheritagechurch.utils.Status
import com.faithdeveloper.believersheritagechurch.viewmodel.ProgrammesViewModel

@Composable
fun ProgrammesScreen(
    programmesViewModel: ProgrammesViewModel,
    retry: () -> Unit,
    mainActivity: MainActivity,
    navigateToPlayingActivity: (message: Message) -> Unit

) {
    val mediaStarted by mainActivity.mediaStarted.observeAsState(false)

    val mediaState by mainActivity.playbackState.observeAsState(PlaybackState.PAUSED)

    val items by programmesViewModel.programmes.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ReusableTop(
            title = "Programmes"
        )

        when (items.type) {
            Status.SUCCESS -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
                ) {
                    items(items.data) { programme ->
                        ProgrammesItemRow(programme = programme)
                    }
                }
            }

            Status.LOADING -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Status.ERROR -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No Internet Connection", color = MaterialTheme.colorScheme.error)
                    Button(
                        onClick = { retry.invoke() },
                        modifier = Modifier.wrapContentSize(align = Alignment.Center)
                    ) {
                        Text(text = "RETRY")
                    }
                }
            }

        }

        if (mediaStarted) {
            PlayingBar(
                mediaState = mediaState,
                message = mainActivity.getMessage()!!,
                playbackClick = {
                    mainActivity.playbackClick()
                },
                barClick = {
                    navigateToPlayingActivity.invoke(mainActivity.getMessage()!!)
                },
                stopPlayback = {
                    mainActivity.stopPlayback()
                })
        }

    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProgrammesItemRow(
    programme: Programme
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        GlideImage(
            alignment = Alignment.Center,
            contentScale = ContentScale.FillBounds,
            model = programme.image_link,
            contentDescription = programme.name,
            modifier = Modifier
                .size(75.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = programme.name,
                style = MaterialTheme.typography.titleMedium
            )
            if (programme.description.isNotBlank()) {
                Text(
                    text = programme.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = programme.day,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = programme.time,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = programme.frequency,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}