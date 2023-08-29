package com.faithdeveloper.believersheritagechurch.ui.aboutdetails

import android.content.res.Configuration
import android.text.Html
import android.util.TypedValue
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.faithdeveloper.believersheritagechurch.R
import com.faithdeveloper.believersheritagechurch.data.about.AboutItem
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.playing.PlaybackState
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.ui.messages.PlayingBar

@Composable
fun AboutDetailsScreen(
    aboutItem: AboutItem,
    mainActivity: MainActivity,
    navigateToPlayingActivity: (message: Message) -> Unit
) {
    val mediaStarted by mainActivity.mediaStarted.observeAsState(false)

    val mediaState by mainActivity.playbackState.observeAsState(PlaybackState.PAUSED)


    Column(modifier = Modifier.fillMaxSize()) {


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 32.dp)
        ) {

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = aboutItem.title,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            items(aboutItem.imagesList) { imageLink ->
                ImageRow(imageLink = imageLink)
            }

            item {
                AndroidView(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .wrapContentHeight(), factory = {
                    TextView(it)
                }, update = {
                    it.typeface = ResourcesCompat.getFont(it.context, R.font.opensans_regular)
                    it.text = Html.fromHtml(aboutItem.description, Html.FROM_HTML_MODE_COMPACT)
                    it.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
                    it.setTextColor(
                        if ((mainActivity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                            mainActivity.resources.getColor(R.color.purple40)
                        } else {
                            mainActivity.resources.getColor(R.color.white)
                        }
                    )
                })
            }
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageRow(imageLink: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        GlideImage(
            modifier = Modifier
                .width(250.dp)
                .height(250.dp)
                .clip(shape = RoundedCornerShape(16.dp)),
            model = imageLink,
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
    }
}