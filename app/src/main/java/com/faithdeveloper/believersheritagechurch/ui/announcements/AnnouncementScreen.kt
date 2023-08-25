package com.faithdeveloper.believersheritagechurch.ui.announcements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.faithdeveloper.believersheritagechurch.data.Util
import com.faithdeveloper.believersheritagechurch.data.announcement.Announcement
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.playing.PlaybackState
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.ui.messages.PlayingBar
import com.faithdeveloper.believersheritagechurch.ui.messages_section.ReusableTop
import kotlinx.coroutines.flow.Flow

@Composable
fun AnnouncementScreen(
    announcement: Flow<PagingData<Announcement>>,
    mainActivity: MainActivity,
    navigateToPlayingActivity: (message: Message) -> Unit,
    onClick: (announcement: Announcement) -> Unit
) {
    val mediaStarted by mainActivity.mediaStarted.observeAsState(false)
    val mediaState by mainActivity.playbackState.observeAsState(PlaybackState.PAUSED)
    val lazyAnnouncements = announcement.collectAsLazyPagingItems()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        ReusableTop(title = "Announcements")

        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (lazyAnnouncements.loadState.refresh) {
                is LoadState.Error -> {
                    item {
                        Column(
                            modifier = Modifier.fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No Internet Connection",
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(
                                onClick = { lazyAnnouncements.retry() },
                                modifier = Modifier.wrapContentSize(align = Alignment.Center)
                            ) {
                                Text(text = "RETRY")
                            }
                        }
                    }
                }

                is LoadState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier.fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }

                    }
                }

                else -> {

                }
            }

            items(items = lazyAnnouncements, key = { it.id }) { announcement ->
                if (announcement != null) {
                    AnnouncementRow(announcement, onClick = onClick)
                }
            }

            when (lazyAnnouncements.loadState.append) {
                is LoadState.Error -> {
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "No Internet Connection",
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(
                                onClick = { lazyAnnouncements.retry() },
                                modifier = Modifier.wrapContentSize(align = Alignment.Center)
                            ) {
                                Text(text = "RETRY")
                            }
                        }
                    }
                }

                is LoadState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                else -> {}
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
fun AnnouncementRow(announcement: Announcement, onClick: (announcement: Announcement) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .clickable {
                onClick.invoke(announcement)
            }
    ) {
        Text(
            modifier = Modifier.padding(bottom = 1.dp),
            text = announcement.title,
            style = MaterialTheme.typography.titleMedium,
        )

        if (announcement.imageLink.isNotBlank()) {
            GlideImage(
                alignment = Alignment.Center,
                model = announcement.imageLink,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }

        if (announcement.description.isNotBlank()) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(
                    1.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 1.dp),
                    text = announcement.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = Util.formatDate(announcement.date),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
