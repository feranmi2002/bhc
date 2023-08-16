package com.faithdeveloper.believersheritagechurch.ui.announcementdetails

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.data.announcement.Announcement

@Composable
fun AnnouncementDetailRoute(announcement: Announcement) {
    AnnouncementDetailsScreen(announcement = announcement)
}