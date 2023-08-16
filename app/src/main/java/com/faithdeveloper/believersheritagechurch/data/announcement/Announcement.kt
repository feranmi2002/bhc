package com.faithdeveloper.believersheritagechurch.data.announcement

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Announcement(
    val id: String,
    val title: String,
    @ServerTimestamp
    val date: Date,
    val description: String,
    val imageLink: String
) : Parcelable {
    constructor() : this("", "", Date(), "", "")

    override fun toString(): String {
        return Uri.encode(Gson().toJson(this))
    }
}
