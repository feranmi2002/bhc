package com.faithdeveloper.believersheritagechurch.navtype

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.faithdeveloper.believersheritagechurch.data.about.AboutItem
import com.faithdeveloper.believersheritagechurch.data.home.ImageSliderImage
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.google.gson.Gson

class ImageSliderNavType: NavType<ImageSliderImage>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ImageSliderImage? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            bundle.getParcelable(key)

    else bundle.getParcelable(key, ImageSliderImage::class.java)
    }

    override fun parseValue(value: String): ImageSliderImage {
        return Gson().fromJson(value, ImageSliderImage::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: ImageSliderImage) {
        bundle.putParcelable(key, value)
    }
}