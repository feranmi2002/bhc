package com.faithdeveloper.believersheritagechurch.navtype

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.faithdeveloper.believersheritagechurch.data.about.AboutItem
import com.faithdeveloper.believersheritagechurch.data.announcement.Announcement
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.google.gson.Gson

class AnnouncementNavType: NavType<Announcement>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Announcement? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            bundle.getParcelable(key)

    else bundle.getParcelable(key, Announcement::class.java)
    }

    override fun parseValue(value: String): Announcement {
        return Gson().fromJson(value, Announcement::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: Announcement) {
        bundle.putParcelable(key, value)
    }
}