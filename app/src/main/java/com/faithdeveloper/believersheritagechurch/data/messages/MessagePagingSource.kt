package com.faithdeveloper.believersheritagechurch.data.messages

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faithdeveloper.believersheritagechurch.data.LoadType
import com.google.firebase.firestore.DocumentSnapshot

class MessagePagingSource(
    private val messageType: String,
    private val repository: MessagesRepository,
    private val loadType: LoadType,
    private val searchText: String? = null
) : PagingSource<DocumentSnapshot, Message>() {
    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Message>): DocumentSnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Message> {
        return try {
            val result = repository.getMessages(
                messageType,
                params.loadSize.toLong(), loadType, params.key,
            )

            LoadResult.Page(
                prevKey = null,
                nextKey = result.key,
                data = if (searchText == null) result.messages
                else {
                    result.messages.filter { message ->
                        message.title.contains(searchText, true)
                    }
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}