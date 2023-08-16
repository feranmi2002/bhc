package com.faithdeveloper.believersheritagechurch.ui.announcementdetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.faithdeveloper.believersheritagechurch.data.Util
import com.faithdeveloper.believersheritagechurch.data.announcement.Announcement
import com.faithdeveloper.believersheritagechurch.ui.messages_section.ReusableTop

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AnnouncementDetailsScreen(announcement: Announcement) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ReusableTop(title = announcement.title)
        if (announcement.imageLink.isNotBlank()) {
            GlideImage(
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 8.dp, top = 16.dp),
                model = announcement.imageLink,
                contentDescription = null
            )
        }

        if (announcement.description.isNotBlank()) {
            Text(
                text = announcement.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            modifier = Modifier.padding(top = 2.dp), text = Util.formatDate(announcement.date),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}