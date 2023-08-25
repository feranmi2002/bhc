package com.faithdeveloper.believersheritagechurch.ui.messages_section

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.faithdeveloper.believersheritagechurch.R
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.messages_section.MessageSectionItems
import com.faithdeveloper.believersheritagechurch.data.playing.PlaybackState
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.ui.messages.PlayingBar
import com.faithdeveloper.believersheritagechurch.utils.Status
import com.faithdeveloper.believersheritagechurch.viewmodel.MessageSectionViewModel

@Composable
fun MessagesSectionScreen(
    modifier: Modifier = Modifier,
    onClick: (messageType: String) -> Unit,
    messageSectionViewModel: MessageSectionViewModel,
    retry: () -> Unit,
    mainActivity: MainActivity,
    navigateToPlayingActivity: (message: Message) -> Unit
) {
    val mediaStarted by mainActivity.mediaStarted.observeAsState(false)
    val mediaState by mainActivity.playbackState.observeAsState(PlaybackState.PAUSED)
    val items by messageSectionViewModel.messageSections.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        ReusableTop(
            title = stringResource(id = R.string.bottom_navigation_messages)
        )

        when (items.type) {
            Status.SUCCESS -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 32.dp)
                ) {
                    items(items.data) { messageSectionItem ->
                        MessageSectionRow(messageItem = messageSectionItem, onClick = onClick)
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
                    CircularProgressIndicator()
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


@Composable
fun ReusableTop(
    modifier: Modifier = Modifier,
    title: String,
) {
    Row(
        modifier.fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MessageSectionRow(
    modifier: Modifier = Modifier,
    messageItem: MessageSectionItems,
    onClick: (messageType: String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .clickable {
                onClick.invoke(messageItem.title)
            }
    ) {
        GlideImage(
            alignment = Alignment.Center,
            model = messageItem.imageLink,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(75.dp)
                .clip(RoundedCornerShape(16.dp))

        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                modifier = Modifier.padding(bottom = 3.dp),
                text = messageItem.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = messageItem.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
