package com.faithdeveloper.believersheritagechurch.ui.messages

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.viewmodel.MessageViewModel


@Composable
fun MessagesRoute(
    messageViewModel: MessageViewModel,
    onClick: (message: Message) -> Unit,
) {
    MessagesScreen(
        messageViewModel = messageViewModel,
        modifier = Modifier,
        onClick = onClick,
        loadTypeSelected = {
            messageViewModel.updateLoadType(it)
        },
        onSearchTextChange = {
            messageViewModel.updateSearchText(it)
        }
    ) {
        messageViewModel.search()
    }
}

