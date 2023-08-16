package com.faithdeveloper.believersheritagechurch.data.announcement

import com.faithdeveloper.believersheritagechurch.data.LoadType
import com.faithdeveloper.believersheritagechurch.data.Util.formatSnapshot
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class AnnouncementRepositoryImpl(val database: FirebaseFirestore) : AnnouncementRepository {
    override suspend fun getAnnouncements(
        loadSize: Long,
        loadType: LoadType,
        key: DocumentSnapshot?
    ): AnnouncementResult {
        val query = if (key == null) {
            database.collection("Announcements").orderBy(
                "date",
                when (loadType) {
                    LoadType.DESCENDING -> com.google.firebase.firestore.Query.Direction.DESCENDING
                    else -> com.google.firebase.firestore.Query.Direction.ASCENDING
                }
            ).limit(loadSize)
        } else {
            database.collection("Announcements").orderBy(
                "date",
                when (loadType) {
                    LoadType.DESCENDING -> com.google.firebase.firestore.Query.Direction.DESCENDING
                    else -> com.google.firebase.firestore.Query.Direction.ASCENDING
                }
            ).limit(loadSize).startAfter(key)
        }
        val result = query.get(Source.SERVER).await()
        return if (result.isEmpty) AnnouncementResult(null, emptyList())
        else AnnouncementResult(key = result.last(), formatSnapshot<Announcement>(result))
    }

}