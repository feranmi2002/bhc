package com.faithdeveloper.believersheritagechurch.data

import android.content.Context
import com.faithdeveloper.believersheritagechurch.data.about.AboutRepository
import com.faithdeveloper.believersheritagechurch.data.about.AboutRepositoryImpl
import com.faithdeveloper.believersheritagechurch.data.announcement.AnnouncementRepository
import com.faithdeveloper.believersheritagechurch.data.announcement.AnnouncementRepositoryImpl
import com.faithdeveloper.believersheritagechurch.data.home.HomeRepository
import com.faithdeveloper.believersheritagechurch.data.home.HomeRepositoryImpl
import com.faithdeveloper.believersheritagechurch.data.messages.MessageRepositoryImpl
import com.faithdeveloper.believersheritagechurch.data.messages.MessagesRepository
import com.faithdeveloper.believersheritagechurch.data.playing.PlayingRepository
import com.faithdeveloper.believersheritagechurch.data.playing.PlayingRepositoryImpl
import com.faithdeveloper.believersheritagechurch.data.programmes.ProgrammesRepository
import com.faithdeveloper.believersheritagechurch.data.programmes.ProgrammesRepositoryImpl
import com.faithdeveloper.believersheritagechurch.download.DownloadRepository
import com.faithdeveloper.believersheritagechurch.download.DownloadRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore

interface AppContainer {
    val messagesRepository: MessagesRepository
    val upcomingRepository: AnnouncementRepository
    val homeRepository: HomeRepository
    val playingRepository: PlayingRepository
    val announcementRepository: AnnouncementRepository
    val aboutRepository: AboutRepository
    val downloadRepository: DownloadRepository
    val programmesRepository: ProgrammesRepository
}

class AppContainerImpl(
    private val applicationContext: Context,
    database: FirebaseFirestore,
) : AppContainer {
    override val aboutRepository: AboutRepository by lazy {
        AboutRepositoryImpl(database)
    }
    override val announcementRepository: AnnouncementRepository by lazy {
        AnnouncementRepositoryImpl(database)
    }
    override val playingRepository: PlayingRepository by lazy {
        PlayingRepositoryImpl(applicationContext)
    }
    override val messagesRepository: MessagesRepository by lazy {
        MessageRepositoryImpl(database)
    }

    override val upcomingRepository: AnnouncementRepository by lazy {
        AnnouncementRepositoryImpl(database = database)
    }
    override val homeRepository: HomeRepository by lazy {
        HomeRepositoryImpl(database, applicationContext)
    }

    override val downloadRepository: DownloadRepository by lazy {
        DownloadRepositoryImpl(applicationContext)
    }
    override val programmesRepository: ProgrammesRepository by lazy {
        ProgrammesRepositoryImpl(database)
    }
}