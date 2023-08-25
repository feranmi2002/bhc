package com.faithdeveloper.believersheritagechurch.ui.messages_section

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.viewmodel.MessageSectionViewModel

@Composable
fun MessagesSectionRoute(
    messageSectionViewModel: MessageSectionViewModel,
    onClick: (messageType: String) -> Unit,
    mainActivity: MainActivity,
    navigateToPlayingActivity:(message: Message) -> Unit
) {
    messageSectionViewModel.getMessageSections()
    MessagesSectionScreen(
        onClick = onClick,
        messageSectionViewModel = messageSectionViewModel,
        retry = {
            messageSectionViewModel.retry()
        },
        mainActivity = mainActivity,
        navigateToPlayingActivity = {
            navigateToPlayingActivity.invoke(it)
        }
    )
}