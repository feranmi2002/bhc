package com.faithdeveloper.believersheritagechurch.data.playing

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.playingservice.PlayingService
import com.faithdeveloper.believersheritagechurch.playingservice.PlayingServiceInterface
import com.faithdeveloper.believersheritagechurch.utils.AppPreferences.getPlayingServiceState
import kotlin.properties.Delegates

class PlayingRepositoryImpl(private val applicationContext: Context) : PlayingRepository,
    RepositoryServiceInterface {
    private lateinit var playingService: PlayingService
    private var mBound = false
    private lateinit var message: Message
    private var viewModel: PlayingServiceInterface? = null
    private var mainActivity: MainActivityPlayingServiceInterface? = null
    private var servicePreviouslyStartedState by Delegates.notNull<Boolean>()
    private var serviceStartedByMainActivity = false

    init {
        servicePreviouslyStartedState = applicationContext.getPlayingServiceState()

    }

    override fun mainActivityInstance(mainActivity: MainActivityPlayingServiceInterface) {
        this.mainActivity = mainActivity
    }

    override fun viewModelInstance(viewModel: PlayingServiceInterface) {
        this.viewModel = viewModel
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayingService.LocalBinder
            playingService = binder.getService()
            mBound = true
            playingService.repositoryInstance(this@PlayingRepositoryImpl)
            if (playingService.aMessageIsAlreadyPlaying()) {
                if (playingService.getMessage().id == message.id) {
                    viewModel?.playbackState(playingService.returnPlaybackState())
                    mainActivity?.playbackState(playingService.returnPlaybackState())
                    mainActivity?.mediaStarted(true)
                } else {
                    stopMediaToRestartAnother(message)
                }
            } else {
                if (serviceStartedByMainActivity) {
//                    no message is currently being played
                    endService()
                }else{
                    playingService.startPlaying(message)
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }
    }

    override fun startService(message: Message, fromMainActivity:Boolean) {
        serviceStartedByMainActivity = fromMainActivity
        this.message = message
        if (servicePreviouslyStartedState) {
            bindService()
        } else {
            this.message = message
            val intent = Intent(applicationContext, PlayingService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                applicationContext.startForegroundService(
                    intent
                )
            } else {
                applicationContext.startService(intent)
            }
            bindService()
        }
    }

    private fun bindService() {
        val intent = Intent(applicationContext, PlayingService::class.java)
        applicationContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }


    override fun unbindService() {
        mainActivity?.mediaStarted(false)
        viewModel?.navigateBackwards()
        applicationContext.unbindService(serviceConnection)
    }

    override fun playbackPosition(): Int = if (this::playingService.isInitialized) {
        playingService.getCurrentPosition()
    } else 0


    override fun pauseMedia() {
        playingService.pauseMedia()
    }

    override fun resumeMedia() {
        playingService.resumeMedia()
    }

    override fun restartMediaAfterError() {
        playingService.restartMediaAfterError()
    }

    override fun restartMediaAfterCompletion() {
        playingService.restartMediaAfterCompletion()
    }

    override fun endService() {
        mainActivity?.mediaStarted(false)
        viewModel?.navigateBackwards()
        playingService.stopMediaPermanently()
    }

    override fun seekTo(position: Float) {
        playingService.seekTo(position)
    }

    override fun playbackState(playbackState: PlaybackState) {
        viewModel?.playbackState(playbackState)
        mainActivity?.playbackState(playbackState)
    }

    override fun pauseDueToSlider() {
        playingService.pauseDueToSeeking()
    }

    override fun setPlayingSpeed(playingSpeed: PlayingSpeed) {
        playingService.setPlayingSpeed(playingSpeed)
    }

    override fun stopMediaToRestartAnother(message: Message) {
        this.message = message
        playingService.stopMediaToRestartAnotherOne()
        playingService.startPlaying(message)
    }

    override fun getMessage() = message

    override fun getPlaybackState() = playingService.returnPlaybackState()

    override fun getPlayingSpeed() = playingService.getPlayingSpeed()

    override fun playingSpeed(playingSpeed: PlayingSpeed) {
        viewModel?.playingSpeed(playingSpeed)
    }
}