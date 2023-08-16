package com.faithdeveloper.believersheritagechurch.data.announcement

import com.faithdeveloper.believersheritagechurch.data.LoadType
import com.google.firebase.firestore.DocumentSnapshot

interface AnnouncementRepository {
    suspend fun getAnnouncements(
        loadSize: Long,
        loadType: LoadType,
        key: DocumentSnapshot?
    ):AnnouncementResult

}