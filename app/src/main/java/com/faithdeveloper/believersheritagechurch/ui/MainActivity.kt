package com.faithdeveloper.believersheritagechurch.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import com.faithdeveloper.believersheritagechurch.data.AppContainerImpl
import com.faithdeveloper.believersheritagechurch.utils.NotificationUtil.createNotificationChannels
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        this.createNotificationChannels()
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val appContainer = AppContainerImpl(this, Firebase.firestore)
        setContent {
            App(appContainer)
        }
        super.onCreate(savedInstanceState)
    }

}