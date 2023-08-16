package com.faithdeveloper.believersheritagechurch.data.messages

import com.faithdeveloper.believersheritagechurch.data.LoadType
import com.faithdeveloper.believersheritagechurch.data.messages_section.MessageSectionItems
import com.faithdeveloper.believersheritagechurch.utils.Result
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    suspend fun messageSectionItems(): Flow<Result<MessageSectionItems>>
    suspend fun getMessages(
        messageType: String,
        loadSize: Long,
        loadType: LoadType,
        key: DocumentSnapshot?
    ): MessageResult
}
