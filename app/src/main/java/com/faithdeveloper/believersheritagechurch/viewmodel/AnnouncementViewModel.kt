package com.faithdeveloper.believersheritagechurch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.faithdeveloper.believersheritagechurch.data.LoadType
import com.faithdeveloper.believersheritagechurch.data.announcement.AnnouncementPagingSource
import com.faithdeveloper.believersheritagechurch.data.announcement.AnnouncementRepository

class AnnouncementViewModel(
    private val repository: AnnouncementRepository,
) : ViewModel() {
    fun announcements(loadType: LoadType = LoadType.DESCENDING) = Pager(
        PagingConfig(pageSize = 30),
        pagingSourceFactory = {
            AnnouncementPagingSource(repository, loadType)
        }
    ).flow.cachedIn(viewModelScope)

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            repository: AnnouncementRepository,
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AnnouncementViewModel(repository) as T
                }
            }
    }
}