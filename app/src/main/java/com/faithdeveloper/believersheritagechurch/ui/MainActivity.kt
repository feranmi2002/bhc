package com.faithdeveloper.believersheritagechurch.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.faithdeveloper.believersheritagechurch.data.AppContainerImpl
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.playing.MainActivityPlayingServiceInterface
import com.faithdeveloper.believersheritagechurch.data.playing.PlaybackState
import com.faithdeveloper.believersheritagechurch.data.playing.PlayingRepository
import com.faithdeveloper.believersheritagechurch.utils.AppPreferences.getPlayingMessage
import com.faithdeveloper.believersheritagechurch.utils.AppPreferences.getPlayingServiceState
import com.faithdeveloper.believersheritagechurch.utils.NotificationUtil.createNotificationChannels
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class MainActivity : ComponentActivity(), MainActivityPlayingServiceInterface {

    var playingRepository: PlayingRepository? = null

    private val _mediaStarted = MutableLiveData(false)

    val mediaStarted: LiveData<Boolean> get() = _mediaStarted

    private val _playbackState = MutableLiveData<PlaybackState>()

    val playbackState: LiveData<PlaybackState> get() = _playbackState

    private var initialPlayingMessage: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        val appContainer = AppContainerImpl(this, Firebase.firestore)
        initialPlayingMessage = applicationContext.getPlayingMessage()
        applicationContext.getPlayingServiceState().run {
            if (this) {
                playingRepository = appContainer.playingRepository
                playingRepository?.mainActivityInstance(this@MainActivity)
                playingRepository?.startService(
                    Gson().fromJson(
                        Uri.decode(initialPlayingMessage),
                        Message::class.java
                    )
                )
                _mediaStarted.value = true
            }
        }

        this.createNotificationChannels()

        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContent {
            App(appContainer, mainActivity = this@MainActivity)
        }
        super.onCreate(savedInstanceState)
    }

    override fun playbackState(playbackState: PlaybackState) {
        _playbackState.value = playbackState
    }

    override fun mediaStarted(state: Boolean) {
        _mediaStarted.value = state
        if (!state) {
            playingRepository = null
        }

    }

    fun getMessage() = playingRepository?.getMessage()

    fun playbackClick() {
        when (playbackState.value) {
            PlaybackState.PLAYING -> {
                playingRepository?.pauseMedia()
            }

            PlaybackState.PAUSED -> {
                playingRepository?.resumeMedia()
            }

            PlaybackState.BUFFERING -> {
//                   do nothing
            }

            PlaybackState.FAILED -> {
                playingRepository?.restartMediaAfterError()
            }

            PlaybackState.FINISHED -> {
                playingRepository?.restartMediaAfterCompletion()
            }

            else -> {

            }
        }
    }

    fun playingRepositorySet(playingRepository: PlayingRepository) {
        if (this.playingRepository == null) {
            this.playingRepository = playingRepository
            this.playingRepository?.mainActivityInstance(this)
            _mediaStarted.value = true
        }
    }

    fun stopPlayback(){
        playingRepository?.endService()
    }
}