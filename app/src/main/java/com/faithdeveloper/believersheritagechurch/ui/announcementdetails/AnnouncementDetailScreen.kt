package com.faithdeveloper.believersheritagechurch.ui.announcementdetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.faithdeveloper.believersheritagechurch.data.Util
import com.faithdeveloper.believersheritagechurch.data.announcement.Announcement
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.playing.PlaybackState
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.ui.messages.PlayingBar
import com.faithdeveloper.believersheritagechurch.ui.messages_section.ReusableTop

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AnnouncementDetailsScreen(
    announcement: Announcement, mainActivity: MainActivity,
    navigateToPlayingActivity: (message: Message) -> Unit
) {
    val mediaStarted by mainActivity.mediaStarted.observeAsState(false)
    val mediaState by mainActivity.playbackState.observeAsState(PlaybackState.PAUSED)

    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = 32.dp)
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ReusableTop(title = announcement.title)

            if (announcement.imageLink.isNotBlank()) {
                GlideImage(
                    modifier = Modifier
                        .size(250.dp)
                        .padding(bottom = 8.dp, top = 16.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    model = announcement.imageLink,
                    contentDescription = null
                )
            }

            if (announcement.description.isNotBlank()) {
                Text(
                    text = announcement.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Text(
                modifier = Modifier.padding(top = 2.dp), text = Util.formatDate(announcement.date),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )

        }

        Column(modifier = Modifier.fillMaxWidth()) {
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
}