package com.faithdeveloper.believersheritagechurch.data.home

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class ImageSliderImage(
    val image_link: String,
    @ServerTimestamp
    val date: Date
) : Parcelable {
    override fun toString(): String {
        return Uri.encode(Gson().toJson(this))
    }
    constructor() : this("", Date())
}
