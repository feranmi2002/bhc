package com.faithdeveloper.believersheritagechurch.ui.home

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.faithdeveloper.believersheritagechurch.data.home.ImageSliderImage
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.playing.PlaybackState
import com.faithdeveloper.believersheritagechurch.ui.AppDestinations
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.ui.messages.PlayingBar
import com.faithdeveloper.believersheritagechurch.utils.Result
import com.faithdeveloper.believersheritagechurch.utils.Status
import com.faithdeveloper.believersheritagechurch.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navigateTo: (destination: AppDestinations) -> Unit,
    onClickSliderImage: (imageSliderImage: ImageSliderImage) -> Unit,
    retryLoadImages: () -> Unit,
    mainActivity: MainActivity,
    navigateToPlayingActivity: (message: Message) -> Unit,
) {
    val mediaStarted by mainActivity.mediaStarted.observeAsState(false)

    val mediaState by mainActivity.playbackState.observeAsState(PlaybackState.PAUSED)

    val images by homeViewModel.imageSliderImages.collectAsStateWithLifecycle()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            TopStrip()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                AutoSlidingCarousel(
                    images = images,
                    onClickSliderImage = { imageSliderImage ->
                        onClickSliderImage.invoke(imageSliderImage)
                    }
                ) {
                    retryLoadImages.invoke()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Sections(navigateTO = navigateTo)
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
}

@Composable
fun TopStrip() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "B.H.C",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun AutoSlidingCarousel(
    autoSlideDuration: Long = 5000L,
    pagerState: PagerState = remember {
        PagerState()
    },
    images: Result<ImageSliderImage>,
    onClickSliderImage: (imageSliderImage: ImageSliderImage) -> Unit,
    retryLoadImages: () -> Unit
) {

    when (images.type) {
        Status.LOADING -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        Status.SUCCESS -> {
            LaunchedEffect(pagerState.currentPage) {
                delay(autoSlideDuration)
                pagerState.scrollToPage((pagerState.currentPage + 1) % images.data.size)

            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                HorizontalPager(pageCount = images.data.size, state = pagerState) { page ->
                    GlideImage(
                        alignment = Alignment.Center,
                        model = images.data[page].image_link,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clickable {
                                onClickSliderImage.invoke(images.data[page])
                            }
                    )
                }
            }
        }

        Status.ERROR -> {
            retryLoadImages.invoke()
        }
    }


}

@Composable
fun Sections(
    navigateTO: (destination: AppDestinations) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(top = 32.dp),
        userScrollEnabled = true,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        columns = GridCells.Fixed(2),
    ) {
        item {
            SectionImage(
                destination = AppDestinations.MessagesSection
            ) { destination ->
                navigateTO.invoke(destination)
            }
        }

        item {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            SectionImage(
                destination = AppDestinations.Announcements
            ) { destination ->
                navigateTO.invoke(destination)
            }
        }

        item {
            SectionImage(
                destination = AppDestinations.Programmes
            ) { destination ->
                navigateTO.invoke(destination)
            }
        }

        item {
            SectionImage(
                destination = AppDestinations.About
            ) { destination ->
                navigateTO.invoke(destination)
            }
        }

    }
}

@Composable
fun SectionImage(
    destination: AppDestinations,
    navigateTO: (destination: AppDestinations) -> Unit
) {

    Surface(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                navigateTO.invoke(destination)
            },
        color = MaterialTheme.colorScheme.onPrimary,
    ) {
        Column(
            modifier = Modifier.size(150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp),
                painter = painterResource(id = destination.icon),
                contentDescription = null
            )
            Text(
                text = destination.title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


