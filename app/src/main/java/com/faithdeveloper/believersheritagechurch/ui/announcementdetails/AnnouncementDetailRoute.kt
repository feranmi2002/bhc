package com.faithdeveloper.believersheritagechurch.ui.announcementdetails

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.data.announcement.Announcement
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.ui.MainActivity

@Composable
fun AnnouncementDetailRoute(
    announcement: Announcement,
    mainActivity: MainActivity,
    navigateToPlayingActivity: (message: Message) -> Unit
) {
    AnnouncementDetailsScreen(announcement = announcement,
        mainActivity = mainActivity,
        navigateToPlayingActivity = {
            navigateToPlayingActivity.invoke(it)
        })
}