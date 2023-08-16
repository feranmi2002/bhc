package com.faithdeveloper.believersheritagechurch.ui.messages_section

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.viewmodel.MessageSectionViewModel

@Composable
fun MessagesSectionRoute(
    messageSectionViewModel: MessageSectionViewModel,
    onClick: (messageType: String) -> Unit,
) {
    messageSectionViewModel.getMessageSections()
    MessagesSectionScreen(
        onClick = onClick,
        messageSectionViewModel = messageSectionViewModel
    ) {
        messageSectionViewModel.retry()
    }
}