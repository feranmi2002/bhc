package com.faithdeveloper.believersheritagechurch.navtype

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.google.gson.Gson

class MessageNavType: NavType<Message>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Message? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            bundle.getParcelable(key)

    else bundle.getParcelable(key, Message::class.java)
    }

    override fun parseValue(value: String): Message {
        return Gson().fromJson(value, Message::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: Message) {
        bundle.putParcelable(key, value)
    }
}