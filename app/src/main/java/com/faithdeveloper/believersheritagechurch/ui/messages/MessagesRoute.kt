package com.faithdeveloper.believersheritagechurch.ui.messages

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.viewmodel.MessageViewModel


@Composable
fun MessagesRoute(
    messageViewModel: MessageViewModel,
    onClick: (message: Message) -> Unit,
    mainActivity: MainActivity,
    navigateToPlayingActivity:(message:Message) -> Unit
) {
    MessagesScreen(
        messageViewModel = messageViewModel,
        onClick = onClick,
        loadTypeSelected = {
            messageViewModel.updateLoadType(it)
        },
        onSearchTextChange = {
            messageViewModel.updateSearchText(it)
        },
        onSearch = {
            messageViewModel.search()
        },
        mainActivity = mainActivity,
        navigateToPlayingActivity = {message ->
            navigateToPlayingActivity.invoke(message)
        }
    )
}

