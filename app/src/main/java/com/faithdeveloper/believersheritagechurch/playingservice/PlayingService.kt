package com.faithdeveloper.believersheritagechurch.playingservice

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaPlayer
import android.media.MediaPlayer.*
import android.net.wifi.WifiManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.faithdeveloper.believersheritagechurch.R
import com.faithdeveloper.believersheritagechurch.data.PlaybackState
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.playing.RepositoryServiceInterface
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.utils.AppPreferences.storePlayingServiceState
import com.faithdeveloper.believersheritagechurch.utils.NotificationUtil
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.properties.Delegates
import kotlin.random.Random

class PlayingService : Service() {

    private val NOTIFICATION_INTENT_KEY = "bhc_intent_key"
    private val NOTIFICATION_ID = 100
    private val REMOTE_INTENT_TYPE = "text/plain"
    private val PLAY_PAUSE_INTENT_DATA = "play_pause"
    private val STOP_MEDIA_INTENT_DATA = "stop_media"
    private val WIFI_LOCK_TAG = "wifi_lock_tag"
    private val binder = LocalBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var audioFocusRequestResult by Delegates.notNull<Int>()
    private lateinit var audioManager: AudioManager
    private lateinit var audioFocusChangeListener: OnAudioFocusChangeListener
    private lateinit var audioFocusRequest: AudioFocusRequest
    private lateinit var audioAttributes: AudioAttributes
    private lateinit var mediaPlayerErrorListener: OnErrorListener
    private lateinit var mediaPlayerBufferingListener: OnBufferingUpdateListener
    private lateinit var mediaPlayerPreparedListener: OnPreparedListener
    private lateinit var mediaOnSeekCompleteListener: OnSeekCompleteListener
    private lateinit var mediaOnCompletionListener: OnCompletionListener
    private lateinit var mediaOnInfoListener: OnInfoListener
    private lateinit var wifiLock: WifiManager.WifiLock
    private lateinit var message: Message
    private var notificationView: RemoteViews? = null
    private val mFocusLOck = Object()
    private lateinit var playingServiceBroadcastReceiver: PlayingServiceBroadcastReceiver
    private lateinit var audioNoisyReceiver: AudioNoisyBroadcastReceiver
    private lateinit var repositoryServiceInterface: RepositoryServiceInterface
    private lateinit var playbackPauseType: PlaybackPauseTypes
    private lateinit var playbackState: PlaybackState
    private lateinit var job: Job
    private lateinit var scope: CoroutineScope

    fun repositoryInstance(repositoryServiceInterface: RepositoryServiceInterface) {
        this.repositoryServiceInterface = repositoryServiceInterface
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {

        job = SupervisorJob()
        scope = CoroutineScope(kotlinx.coroutines.Dispatchers.IO + job)

        scope.launch {
            applicationContext.storePlayingServiceState(started = true)
        }

        playbackPauseType = PlaybackPauseTypes.PLAY

        audioManager = getSystemService((Context.AUDIO_SERVICE)) as AudioManager

        audioFocusChangeListener =
            OnAudioFocusChangeListener { focusChange ->
                when (focusChange) {
                    AudioManager.AUDIOFOCUS_GAIN -> {
                        if (playbackPauseType == PlaybackPauseTypes.TEMPORAL_PAUSE ||
                            playbackPauseType == PlaybackPauseTypes.PLAY
                        ) {
                            resumeMedia()
                        }
                    }
                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                        if (playbackPauseType == PlaybackPauseTypes.PLAY) {
                            playbackPauseType = PlaybackPauseTypes.TEMPORAL_PAUSE
                            mediaPlayer?.pause()
                        }
                    }
                    AudioManager.AUDIOFOCUS_LOSS -> {
                        pauseMedia()
                    }
                }
            }

        audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(audioFocusChangeListener)
                .build()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequestResult = audioManager.requestAudioFocus(audioFocusRequest)
        }

        mediaPlayerErrorListener =
            OnErrorListener { mp, what, extra ->
                mediaPlaybackError()
                return@OnErrorListener true
            }

        mediaPlayerBufferingListener = OnBufferingUpdateListener { mp, percent ->

        }

        mediaOnInfoListener = OnInfoListener { mp, what, extra ->
            when (what) {
                MEDIA_INFO_BUFFERING_START -> {
                    setPlaybackState(PlaybackState.BUFFERING)
                }

                MEDIA_INFO_BUFFERING_END -> {
                    setPlaybackState(PlaybackState.PLAYING)
                }
            }
            return@OnInfoListener true
        }

        mediaPlayerPreparedListener = OnPreparedListener { mediaPlayer ->
            synchronized(mFocusLOck) {
                if (audioFocusRequestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    setPlaybackState(PlaybackState.PLAYING)
                    mediaPlayer.start()
                    playbackPauseType = PlaybackPauseTypes.PLAY
                    notificationView?.setImageViewResource(
                        R.id.play_pause,
                        R.drawable.ic_round_pause_24
                    )
                    NotificationManagerCompat.from(this@PlayingService).apply {
                        notify(NOTIFICATION_ID, buildNotification().build())
                    }
                }
            }
        }

        mediaOnSeekCompleteListener = OnSeekCompleteListener { mediaPlayer ->
            mediaPlayer.start()
            playbackPauseType = PlaybackPauseTypes.PLAY
            setPlaybackState(PlaybackState.PLAYING)
        }

        mediaOnCompletionListener = OnCompletionListener {
            mediaFinishedPlayback()
        }

        wifiLock = (applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager)
            .createWifiLock(WifiManager.WIFI_MODE_FULL, WIFI_LOCK_TAG)

        playingServiceBroadcastReceiver = PlayingServiceBroadcastReceiver()

        ContextCompat.registerReceiver(
            applicationContext,
            playingServiceBroadcastReceiver,
            IntentFilter.create(packageName, REMOTE_INTENT_TYPE),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        audioNoisyReceiver = AudioNoisyBroadcastReceiver()

        ContextCompat.registerReceiver(
            applicationContext,
            audioNoisyReceiver,
            IntentFilter(),
            ContextCompat.RECEIVER_EXPORTED
        )

        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, buildNotification().build())
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onDestroy() {
        super.onDestroy()
    }


    inner class LocalBinder : Binder() {
        fun getService(): PlayingService = this@PlayingService
    }

    fun startPlaying(message: Message) {
        this.message = message
        updateNotificationLayout()

        setPlaybackState(PlaybackState.BUFFERING)

        mediaPlayer = MediaPlayer().apply {
            isLooping = false
            setAudioAttributes(
                audioAttributes
            )
            setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
            setOnBufferingUpdateListener(mediaPlayerBufferingListener)
            setOnErrorListener(mediaPlayerErrorListener)
            setOnPreparedListener(mediaPlayerPreparedListener)
            setOnSeekCompleteListener(mediaOnSeekCompleteListener)
            setOnCompletionListener(mediaOnCompletionListener)
            setOnInfoListener(mediaOnInfoListener)
            try {
                setDataSource(message.audioLink)
                prepareAsync()
            } catch (exception: Exception) {
                mediaPlaybackError()
            }
        }
    }

    fun getMessage() = message

    fun aMessageIsAlreadyPlaying() = mediaPlayer != null

    fun getCurrentPosition() = mediaPlayer?.currentPosition ?: 0

    fun pauseMedia() {
        playbackPauseType = PlaybackPauseTypes.DELIBERATE_PAUSE
        mediaPlayer?.pause()
        if (wifiLock.isHeld) {
            wifiLock.release()
        }
        notificationView?.setImageViewResource(
            R.id.play_pause,
            R.drawable.ic_round_play_arrow_24
        )
        NotificationManagerCompat.from(this@PlayingService).apply {
            notify(NOTIFICATION_ID, buildNotification().build())
        }
        setPlaybackState(PlaybackState.PAUSED)
        playbackPauseType = PlaybackPauseTypes.DELIBERATE_PAUSE

    }

    fun seekTo(position: Float) {
        setPlaybackState(PlaybackState.BUFFERING)
        mediaPlayer?.seekTo(position.toInt())
    }

    fun stopMediaToRestartAnotherOne() {
        mediaPlayer?.stop()
        mediaPlayer = null
    }

    fun stopMediaPermanently() {
        mediaPlayer?.stop()
        if (wifiLock.isHeld) {
            wifiLock.release()
        }
        releaseMediaPlayer()
        stopPlayingService()
    }

    fun pauseDueToSeeking() {
        mediaPlayer?.pause()
        playbackPauseType = PlaybackPauseTypes.DELIBERATE_PAUSE
        setPlaybackState(PlaybackState.PAUSED)
    }

    private fun mediaFinishedPlayback() {
        setPlaybackState(PlaybackState.FINISHED)
        if (wifiLock.isHeld) {
            wifiLock.release()
        }
        notificationView?.setImageViewResource(
            R.id.play_pause,
            R.drawable.ic_round_play_arrow_24
        )
        NotificationManagerCompat.from(this@PlayingService).apply {
            notify(NOTIFICATION_ID, buildNotification().build())
        }
    }

    private fun stopPlayingService() {
//        in case the call to close service is from notification
        repositoryServiceInterface.unbind()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        scope.launch {
            applicationContext.storePlayingServiceState(started = false)
        }
        applicationContext.unregisterReceiver(playingServiceBroadcastReceiver)
        applicationContext.unregisterReceiver(audioNoisyReceiver)
        job.cancel()
        stopSelf()
    }

    private fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun resumeMedia() {
        synchronized(mFocusLOck) {
            if (audioFocusRequestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                wifiLock.acquire()
                mediaPlayer?.start()
                setPlaybackState(PlaybackState.PLAYING)
                playbackPauseType = PlaybackPauseTypes.PLAY
                notificationView?.setImageViewResource(
                    R.id.play_pause,
                    R.drawable.ic_round_pause_24
                )
                NotificationManagerCompat.from(this@PlayingService).apply {
                    notify(NOTIFICATION_ID, buildNotification().build())
                }
            }
        }
    }

    fun mediasIsPlaying() = mediaPlayer?.isPlaying ?: false

    fun restartMediaAfterError() {
        startPlaying(message)
    }

    fun restartMediaAfterCompletion() {
        resumeMedia()
    }

    private fun mediaPlaybackError() {
        if (wifiLock.isHeld) {
            wifiLock.release()
        }
        notificationView?.setImageViewResource(
            R.id.play_pause,
            R.drawable.ic_round_play_arrow_24
        )
        NotificationManagerCompat.from(this@PlayingService).apply {
            notify(NOTIFICATION_ID, buildNotification().build())
        }
        setPlaybackState(PlaybackState.FAILED)
    }

    private fun setPlaybackState(playbackState: PlaybackState) {
        this.playbackState = playbackState
        repositoryServiceInterface.playbackState(playbackState)
    }


    private fun buildContentIntent(): PendingIntent? {
        val appIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("$packageName NEW_ACTIVITY", Gson().toJson(message))
            action = Random.nextInt().toString()
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            this,
            300,
            appIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
    }

    private fun buildNotificationLayout(): RemoteViews {
        if (notificationView == null) {
            notificationView = RemoteViews(packageName, R.layout.playing_message)
        }
        val playPauseIntent = Intent(packageName).apply {
            type = REMOTE_INTENT_TYPE
            putExtra(NOTIFICATION_INTENT_KEY, PLAY_PAUSE_INTENT_DATA)
        }
        val playPausePendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            100,
            playPauseIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        notificationView!!.setOnClickPendingIntent(R.id.play_pause, playPausePendingIntent)

        val closeIntent = Intent(packageName).apply {
            type = REMOTE_INTENT_TYPE
            putExtra(NOTIFICATION_INTENT_KEY, STOP_MEDIA_INTENT_DATA)
        }
        val closePendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            200,
            closeIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        notificationView!!.setOnClickPendingIntent(R.id.close, closePendingIntent)

        return notificationView!!
    }

    private fun loadGlideImageForNotification() {
        getGlideImageForNotification()
    }

    private fun getGlideImageForNotification() {
        scope.launch {
            Glide.with(this@PlayingService)
                .asBitmap()
                .addListener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        loadGlideImageForNotification()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (resource != null) {
                            updateNotificationWithImage(resource)
                        }
                        return false
                    }
                })
                .load(message.imageLink)
                .submit()
        }
    }

    private fun updateNotificationWithImage(image: Bitmap) {
        notificationView!!.setImageViewBitmap(R.id.image, image)
    }

    private fun updateNotificationLayout() {
        notificationView!!.setTextViewText(R.id.title, message.title)
        notificationView!!.setTextViewText(R.id.preacher, message.preacher)
        val notification = buildNotification()
            .setContentIntent(buildContentIntent())
            .build()
        NotificationManagerCompat.from(this).apply {
            notify(NOTIFICATION_ID, notification)
        }
        loadGlideImageForNotification()
    }


    private fun buildNotification(): NotificationCompat.Builder {
        return NotificationCompat.Builder(
            this,
            NotificationUtil.PLAYING_MESSAGE_NOTIFICATION_CHANNEL_ID
        )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)
            .setCustomContentView(buildNotificationLayout())
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setAutoCancel(false)
            .setChannelId(NotificationUtil.PLAYING_MESSAGE_NOTIFICATION_CHANNEL_ID)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setSilent(true)
    }

    inner class PlayingServiceBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action.equals(packageName)) {
                if (intent?.getStringExtra(NOTIFICATION_INTENT_KEY)
                        .equals(PLAY_PAUSE_INTENT_DATA)
                ) {
                    if (playbackState != PlaybackState.BUFFERING) {
                        if (mediaPlayer?.isPlaying == true) {
                            pauseMedia()
                        } else {
                            resumeMedia()
                        }
                    }
                } else if (intent?.getStringExtra(NOTIFICATION_INTENT_KEY)
                        .equals(STOP_MEDIA_INTENT_DATA)
                ) {
                    stopMediaPermanently()
                } else {
//                    do nothing
                }
            }
        }
    }

    inner class AudioNoisyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                if (mediaPlayer?.isPlaying == true) {
                    pauseMedia()
                }
            }
        }
    }

    companion object {
        const val PLAYING_SERVICE_ID = "playing_service"
    }

    enum class PlaybackPauseTypes {
        DELIBERATE_PAUSE,
        TEMPORAL_PAUSE,
        PLAY
    }
}