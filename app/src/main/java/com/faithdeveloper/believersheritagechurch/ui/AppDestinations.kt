package com.faithdeveloper.believersheritagechurch.ui

import androidx.annotation.DrawableRes
import com.faithdeveloper.believersheritagechurch.R

sealed class AppDestinations(val route: String,  val title: String, @DrawableRes val icon: Int) {
    object AnnouncementDetail : AppDestinations(
        "announcememnt_detail",
        "",
        R.drawable.ic_launcher_foreground
    )
    object Playing : AppDestinations(
        "playing",
        "",
        R.drawable.ic_launcher_foreground
    )
    object Home : AppDestinations(
        "home",
        "Home",
        R.drawable.bible_5006
    )

    object ImageViewer : AppDestinations(
        "imageviewer",
        "imageviewer",
        R.drawable.bible_5006
    )

    object MessagesSection : AppDestinations(
        "messagesSectionItem",
        "Sermons",
        R.drawable.bible_5006
    )

    object About : AppDestinations(
        "about",
        "About",
        R.drawable.ic_baseline_church_24
    )

    object AboutDetails : AppDestinations(
        "about_details",
        "About Details",
        R.drawable.ic_baseline_church_24
    )

    object Messages : AppDestinations(
        "messages",
        "Messages",
        R.drawable.bible_5006
    )

    object Announcements : AppDestinations(
        "announcements",
        "Announcements",
        R.drawable.ic_baseline_notifications_24
    )

    object Programmes : AppDestinations(
        "programmes",
        "Programmes",
        R.drawable.ic_round_event_24
    )
}