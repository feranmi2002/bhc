package com.faithdeveloper.believersheritagechurch.ui.messages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.faithdeveloper.believersheritagechurch.R
import com.faithdeveloper.believersheritagechurch.data.LoadType
import com.faithdeveloper.believersheritagechurch.data.Util
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.playing.PlaybackState
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.viewmodel.MessageViewModel


@Composable
fun MessagesScreen(
    messageViewModel: MessageViewModel,
    onClick: (message: Message) -> Unit,
    loadTypeSelected: (loadType: LoadType) -> Unit,
    onSearchTextChange: (text: String) -> Unit,
    onSearch: () -> Unit,
    mainActivity: MainActivity,
    navigateToPlayingActivity: (message:Message) -> Unit

) {
    val mediaStarted by mainActivity.mediaStarted.observeAsState(false)

    val mediaState by mainActivity.playbackState.observeAsState(PlaybackState.PAUSED)

    val lazyPagingMessages = messageViewModel.messages.collectAsLazyPagingItems()

    val loadType by messageViewModel.loadType.collectAsState()

    val searchText by messageViewModel.searchText.collectAsState()

    var searchVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (searchVisible) {
            Search(
                searchText = searchText,
                onSearchTextChange = {
                    onSearchTextChange.invoke(it)
                },
                onCloseSearch = {
                    searchVisible = false
                    onSearchTextChange.invoke("")
                },
                onSearch = {
                    onSearch.invoke()
                },
                title = messageViewModel.messageType
            )
        } else {
            Top(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 16.dp),
                title = messageViewModel.messageType
            ) {
                searchVisible = true
            }
        }

        ChipGroup(selectedType = loadType, onSelectedChange = {
            loadTypeSelected.invoke(it)
        })

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .weight(1f),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (lazyPagingMessages.loadState.refresh) {
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
                                onClick = { lazyPagingMessages.retry() },
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
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                    }
                }

                else -> {
                    items(items = lazyPagingMessages, key = { it.id }) { message ->
                        if (message != null) {
                            MessagesRow(message = message, onClick = onClick)
                        }
                    }
                }
            }

            when (lazyPagingMessages.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .wrapContentHeight(align = Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No Internet Connection",
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(
                                onClick = { lazyPagingMessages.retry() },
                                modifier = Modifier.wrapContentSize(align = Alignment.Center)
                            ) {
                                Text(text = "RETRY")
                            }
                        }
                    }
                }
                else -> Unit
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
fun MessagesRow(message: Message, onClick: (message: Message) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .clickable {
                onClick.invoke(message)
            }
    ) {
        GlideImage(
            alignment = Alignment.Center,
            model = message.imageLink,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(16.dp))

        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(1.dp, alignment = Alignment.CenterVertically)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 1.dp),
                text = message.title,
                style = MaterialTheme.typography.titleMedium
            )
            if (message.description.isNotBlank()) {
                Text(
                    modifier = Modifier.padding(bottom = 1.dp),
                    text = message.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                modifier = Modifier.padding(bottom = 1.dp),
                text = "By: ${message.preacher}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                modifier = Modifier.padding(bottom = 1.dp),
                text = Util.formatDuration(message.length),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = Util.formatDate(message.date),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun Chip(
    loadType: LoadType,
    isSelected: Boolean = false,
    onSelectionChanged: (loadType: LoadType) -> Unit
) {
    Surface(
        modifier = Modifier.padding(4.dp),
        tonalElevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary
    ) {
        Row(
            modifier = Modifier
                .toggleable(
                    value = isSelected,
                    onValueChange = {
                        onSelectionChanged.invoke(loadType)
                    }
                )
        ) {
            Text(
                text = when (loadType) {
                    LoadType.ASCENDING -> "Older"
                    LoadType.DESCENDING -> "Latest"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ChipGroup(
    loadTypes: List<LoadType> = listOf(LoadType.DESCENDING, LoadType.ASCENDING),
    selectedType: LoadType,
    onSelectedChange: (loadType: LoadType) -> Unit
) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        LazyRow {
            items(loadTypes) { it ->
                Chip(
                    loadType = it,
                    onSelectionChanged = {
                        onSelectedChange.invoke(it)
                    },
                    isSelected = selectedType == it
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(
    searchText: String,
    onSearchTextChange: (text: String) -> Unit,
    onCloseSearch: () -> Unit,
    onSearch: () -> Unit,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(),
            value = searchText,
            onValueChange = {
                onSearchTextChange.invoke(it)
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            ) ,
            placeholder = { Text(text = "Search $title") },
            singleLine = true,
            keyboardActions = KeyboardActions(onSearch = {
                if (searchText.isNotBlank()) {
                    onSearch.invoke()
                }
            }),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            )
        )

        Image(
            modifier = Modifier
                .wrapContentWidth()
                .clickable {
                    onCloseSearch.invoke()
                }
                .padding(start = 8.dp),
            painter = painterResource(id = R.drawable.ic_baseline_search_24),
            contentDescription = "Close Search"
        )

    }
}

@Composable
fun Top(
    modifier: Modifier = Modifier,
    title: String,
    searchVisible: () -> Unit
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Image(modifier = Modifier.clickable {
            searchVisible.invoke()
        }, painter = painterResource(id = R.drawable.ic_baseline_search_24), contentDescription = null)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlayingBar(
    mediaState: PlaybackState,
    message: Message,
    playbackClick: () -> Unit,
    stopPlayback: () -> Unit,
    barClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                barClick.invoke()
            },
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (mediaState == PlaybackState.BUFFERING) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Image(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable {
                            playbackClick.invoke()
                        },
                    painter = painterResource(
                        id = when (mediaState) {
                            PlaybackState.PLAYING -> R.drawable.ic_round_pause_24
                            PlaybackState.PAUSED -> R.drawable.ic_round_play_arrow_24
                            PlaybackState.FINISHED -> R.drawable.ic_round_play_arrow_24
                            PlaybackState.FAILED -> R.drawable.ic_round_play_arrow_24
                            else -> 0
                        }

                    ), contentDescription = null
                )
            }

            Text(
                text = message.title,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Image(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .clickable {
                        stopPlayback.invoke()
                    },
                painter = painterResource(id = R.drawable.ic_baseline_close_24),
                contentDescription = "Close"
            )

            GlideImage(
                model = message.imageLink,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(4.dp))
            )


        }
    }
}