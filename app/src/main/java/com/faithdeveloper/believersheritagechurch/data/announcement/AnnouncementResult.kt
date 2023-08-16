package com.faithdeveloper.believersheritagechurch.data.announcement

import com.google.firebase.firestore.DocumentSnapshot

data class AnnouncementResult(
    val key:DocumentSnapshot?,
    val announcements:List<Announcement>
)
