package com.faithdeveloper.believersheritagechurch.data.messages

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Message(
    val id:String,
    val title:String,
    val preacher:String,
    @ServerTimestamp
    val date: Date,
    val venue:String,
    val length:Long,
    val description:String,
    val audioLink:String,
    val imageLink:String,
    val type:String,
    val size:Long,
    val audioType:String
) : Parcelable {
    constructor():this("", "", "", Date(), "", 0L, "", "", "", "", 0L, "")

    override fun toString(): String {
        return Uri.encode(Gson().toJson(this))
    }
}