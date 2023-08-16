package com.faithdeveloper.believersheritagechurch.data.about

import android.net.Uri
import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class AboutItem(
    var title:String,
    var description:String,
    var imagesList:List<String>,
    var id:String
) : Parcelable {
    constructor():this("","", listOf(), "")

    override fun toString(): String {
        return Uri.encode(Gson().toJson(this))
    }
}
