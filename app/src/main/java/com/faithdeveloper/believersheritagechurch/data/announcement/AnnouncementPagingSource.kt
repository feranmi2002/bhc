package com.faithdeveloper.believersheritagechurch.data.announcement

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faithdeveloper.believersheritagechurch.data.LoadType
import com.google.firebase.firestore.DocumentSnapshot

class AnnouncementPagingSource(
    private val repository: AnnouncementRepository,
    private val loadType: LoadType
) : PagingSource<DocumentSnapshot, Announcement>() {
    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Announcement>): DocumentSnapshot? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Announcement> {
        return try {
            val result = repository.getAnnouncements(
                params.loadSize.toLong(),loadType, params.key,
            )
            LoadResult.Page(
                prevKey = null,
                nextKey = result.key,
                data = result.announcements
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}