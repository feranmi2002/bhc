package com.faithdeveloper.believersheritagechurch.ui.announcements

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.data.announcement.Announcement
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.viewmodel.AnnouncementViewModel

@Composable
fun AnnouncementRoute(
    announcementViewModel: AnnouncementViewModel,
    mainActivity: MainActivity,
    navigateToPlayingActivity: (message: Message) -> Unit,
    onClick: (announcement: Announcement) -> Unit
) {
    AnnouncementScreen(
        announcementViewModel.announcements(
        ),
        mainActivity = mainActivity,
        navigateToPlayingActivity = {
            navigateToPlayingActivity.invoke(it)
        }, onClick = onClick
    )

}

