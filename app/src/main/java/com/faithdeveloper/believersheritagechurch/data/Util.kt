package com.faithdeveloper.believersheritagechurch.data

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.faithdeveloper.believersheritagechurch.data.messages_section.MessageSectionItems
import com.faithdeveloper.believersheritagechurch.data.programmes.Programme
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObjects
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Util {

    inline fun <reified T : Any> formatSnapshot(querySnapshot: QuerySnapshot) =
        querySnapshot.toObjects<T>()

    inline fun <reified T : Any> gsonConverterToString(list: List<T>): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    inline fun <reified T> gsonConverterToList(json: String): List<T> {
        return Gson().fromJson(json, object : TypeToken<List<T>>() {}).toList()
    }

    fun formatDuration(lengthInMilliseconds: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(lengthInMilliseconds)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(lengthInMilliseconds - (hours * 3600000))
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(lengthInMilliseconds - ((hours * 3600000) + (minutes * 60000)))

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun formatDate(date: Date): String {
        return SimpleDateFormat("EEEE, dd-MMM-yyyy", Locale.getDefault()).format(date)
    }


    val defaultProgrammes = listOf(
        Programme(
            day = "Wednesday",
            description = "",
            frequency = "Weekly",
            image_link = "",
            name = "Bible Study",
            time = "5:00 pm - 6:30 pm"
        ),
        Programme(
            day = "Sunday",
            description = "",
            frequency = "Bimonthly",
            image_link = "",
            name = "Couples' Programme",
            time = "2:00 pm - 5:00 pm"
        ),
        Programme(
            day = "Friday",
            description = "",
            frequency = "Biweekly",
            image_link = "",
            name = "Night Vigil",
            time = "11:00 pm - 2:00 am"
        ),
        Programme(
            day = "Sunday",
            description = "",
            frequency = "Bimonthly",
            image_link = "",
            name = "Fathers' Programme",
            time = "2:00 pm - 5:00 pm"
        ),
        Programme(
            day = "Sunday",
            description = "",
            frequency = "Bimonthly",
            image_link = "",
            name = "Singles' Programme",
            time = "2:00 pm - 5:00 pm"
        ),
        Programme(
            day = "Sunday",
            description = "",
            frequency = "Weekly",
            image_link = "",
            name = "Sunday School",
            time = "8:00 am - 9:00 am"
        )
    )
    val homeDefaultSections = listOf(
        MessageSectionItems(
            "https://firebasestorage.googleapis.com/v0/b/bhcapp-9c546.appspot.com/o/Images%2FSunday%20School%2Fsunday-school.png?alt=media&token=21222b87-ad75-4eb1-af28-723dd5b7ae1a",
            "Sunday School",
            "Sunday School"
        ),
        MessageSectionItems(
            "https://firebasestorage.googleapis.com/v0/b/bhcapp-9c546.appspot.com/o/Images%2FSunday%20Sermon%2Fsunday_sermon.webp?alt=media&token=4273fcb2-ff22-4848-b910-d2a4f5c3b632",
            "Sunday Sermon",
            "Sunday Sermon"
        ),
        MessageSectionItems(
            "https://firebasestorage.googleapis.com/v0/b/bhcapp-9c546.appspot.com/o/Images%2FNight%20Vigil%2Fnight%20vigil.jpg?alt=media&token=862c7439-0587-4a61-beab-4475bd2d23d4",
            "Night Vigil",
            "Night Vigil"
        ),
        MessageSectionItems(
            "https://firebasestorage.googleapis.com/v0/b/bhcapp-9c546.appspot.com/o/Images%2FBible%20Study%2FBible-Study-Coffee-Explore-the-Bible-2-1024x576.jpg?alt=media&token=4e9cd7ef-2e05-4b29-9fea-83af0b7457f6",
            "Bible Study",
            "Bible Study"
        ),
        MessageSectionItems(
            "https://firebasestorage.googleapis.com/v0/b/bhcapp-9c546.appspot.com/o/Images%2FSingles%2Fmeetsinglse.jpg?alt=media&token=ee473e15-42d7-4d55-95ae-7a993e72de04",
            "Singles",
            "Singles"
        ),
        MessageSectionItems(
            "https://firebasestorage.googleapis.com/v0/b/bhcapp-9c546.appspot.com/o/Images%2FFathers%2Ffather-child-sunset.webp?alt=media&token=eb08fceb-3de6-4527-a11e-eae1dda4eaf3",
            "Fathers",
            "Fathers"
        ),
        MessageSectionItems(
            "https://firebasestorage.googleapis.com/v0/b/bhcapp-9c546.appspot.com/o/Images%2FCouples%2Fportrait-romantic-couple-holding-hands-260nw-764210827.webp?alt=media&token=d3e78077-fcaf-4d1b-8c4d-a980ea6fd8fc",
            "Couples",
            "Couples"
        ),
        MessageSectionItems(
            "https://firebasestorage.googleapis.com/v0/b/bhcapp-9c546.appspot.com/o/Images%2FChildren%2Fchildren.jpg?alt=media&token=e57088fa-1403-487c-993b-8334272c1871",
            "Children",
            "Sunday Sermon"
        ),
        MessageSectionItems(
            "https://firebasestorage.googleapis.com/v0/b/bhcapp-9c546.appspot.com/o/Images%2FOutreaches%2Foutreach-hands-ss-1920.jpg?alt=media&token=c638ec5c-753e-4da0-bf51-1150d9abac2e",
            "Outreaches",
            ""
        ),
        MessageSectionItems(
            "https://firebasestorage.googleapis.com/v0/b/bhcapp-9c546.appspot.com/o/Images%2FWomen%2Fordained-women.1--1500x430.jpg?alt=media&token=8211a195-3879-4e2c-adf3-596c9153902f",
            "Women",
            "Sunday Sermon"
        ),
    )
}