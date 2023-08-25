package com.faithdeveloper.believersheritagechurch.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

object AppPreferences {

    private const val PLAYING_SERVICE_PREFERENCE = "playing_service_preference_state"

    private const val PLAYING_MESSAGE_PREFERENCE = "playing_message"

    fun Context.storePlayingServiceState(started: Boolean) {
        getSharedPreferences(packageName, MODE_PRIVATE).edit()
            .putBoolean(PLAYING_SERVICE_PREFERENCE, started).apply()
    }

    fun Context.getPlayingServiceState() =
        getSharedPreferences(packageName, MODE_PRIVATE).getBoolean(
            PLAYING_SERVICE_PREFERENCE,
            false
        )


    fun Context.storePlayingMessage(message: String) {
        getSharedPreferences(packageName, MODE_PRIVATE).edit()
            .putString(PLAYING_MESSAGE_PREFERENCE, message).apply()
    }

    fun Context.getPlayingMessage() = getSharedPreferences(packageName, MODE_PRIVATE).getString(
        PLAYING_MESSAGE_PREFERENCE, null
    )

}