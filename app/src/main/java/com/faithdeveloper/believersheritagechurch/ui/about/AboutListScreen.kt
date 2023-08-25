package com.faithdeveloper.believersheritagechurch.ui.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithdeveloper.believersheritagechurch.data.about.AboutItem
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.playing.PlaybackState
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.ui.messages.PlayingBar
import com.faithdeveloper.believersheritagechurch.ui.messages_section.ReusableTop
import com.faithdeveloper.believersheritagechurch.utils.Status
import com.faithdeveloper.believersheritagechurch.viewmodel.AboutViewModel

@Composable
fun AboutListScreen(
    aboutViewModel: AboutViewModel,
    itemClick: (aboutItem: AboutItem) -> Unit,
    retry: () -> Unit,
    mainActivity: MainActivity,
    navigateToPlayingActivity: (message: Message) -> Unit
) {
    val mediaStarted by mainActivity.mediaStarted.observeAsState(false)
    val mediaState by mainActivity.playbackState.observeAsState(PlaybackState.PAUSED)
    val aboutItems by aboutViewModel.aboutList.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        ReusableTop(title = "About")

        when (aboutItems.type) {

            Status.SUCCESS -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 32.dp)
                ) {
                    items(aboutItems.data) { aboutItem ->
                        AboutListItemRow(aboutItem = aboutItem, itemClick = {
                            itemClick.invoke(aboutItem)
                        })
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
fun AboutListItemRow(aboutItem: AboutItem, itemClick: (aboutItem: AboutItem) -> Unit) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(align = Alignment.CenterVertically)
        .padding(8.dp)
        .clickable {
            itemClick.invoke(aboutItem)
        }
    ) {
        Text(
            modifier = Modifier.clickable { itemClick.invoke(aboutItem) },
            text = aboutItem.title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}