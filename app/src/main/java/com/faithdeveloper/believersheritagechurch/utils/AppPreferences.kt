package com.faithdeveloper.believersheritagechurch.utils

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.faithdeveloper.believersheritagechurch.data.announcement.AnnouncementLatestDate.Companion.ANNOUNCEMENT_LATEST_DATE_ID
import com.faithdeveloper.believersheritagechurch.playingservice.PlayingService.Companion.PLAYING_SERVICE_ID
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.util.*

object AppPreferences {
    private val PLAYING_SERVICE_STARTED = booleanPreferencesKey(PLAYING_SERVICE_ID)
    private val LATEST_ANNOUNCEMENT_DATE_CLICKED = longPreferencesKey(ANNOUNCEMENT_LATEST_DATE_ID)


    private const val APP_PREFERENCES = "bhc_preferences"

    private val Context.datastore by preferencesDataStore(
        name = APP_PREFERENCES
    )

    suspend fun Context.storePlayingServiceState(started: Boolean) {
        datastore.edit { mutablePreferences ->
            mutablePreferences[PLAYING_SERVICE_STARTED] = started
        }
    }

    fun Context.getPlayingServiceState(): Boolean {
        var state = false
        datastore.data.map { value: Preferences ->
            state = value[PLAYING_SERVICE_STARTED] ?: false
        }
        return state
    }
}