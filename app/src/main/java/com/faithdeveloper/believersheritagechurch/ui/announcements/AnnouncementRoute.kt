package com.faithdeveloper.believersheritagechurch.ui.announcements

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.data.announcement.Announcement
import com.faithdeveloper.believersheritagechurch.viewmodel.AnnouncementViewModel

@Composable
fun AnnouncementRoute(
    announcementViewModel: AnnouncementViewModel,

    onClick: (announcement: Announcement) -> Unit
) {
AnnouncementScreen(
    announcementViewModel.announcements(
    ), onClick = onClick
)

}

