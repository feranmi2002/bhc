package com.faithdeveloper.believersheritagechurch.navtype

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.faithdeveloper.believersheritagechurch.data.about.AboutItem
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.google.gson.Gson

class AboutNavType: NavType<AboutItem>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): AboutItem? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            bundle.getParcelable(key)

    else bundle.getParcelable(key, AboutItem::class.java)
    }

    override fun parseValue(value: String): AboutItem {
        return Gson().fromJson(value, AboutItem::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: AboutItem) {
        bundle.putParcelable(key, value)
    }
}