package com.faithdeveloper.believersheritagechurch.data.announcement

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class AnnouncementLatestDate(
    @ServerTimestamp
    val date: Date
){
    constructor() : this(Date())
    companion object{
        const val ANNOUNCEMENT_LATEST_DATE_ID = "announcement_latest_id"
        const val ANNOUNCEMENT_LATEST_DATE_ID_CLICKED = "announcement_latest_id_clicked"
    }
}
