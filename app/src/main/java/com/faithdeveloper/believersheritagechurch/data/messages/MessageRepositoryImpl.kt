package com.faithdeveloper.believersheritagechurch.data.messages

import com.faithdeveloper.believersheritagechurch.data.LoadType
import com.faithdeveloper.believersheritagechurch.data.Util.formatSnapshot
import com.faithdeveloper.believersheritagechurch.data.messages_section.MessageSectionItems
import com.faithdeveloper.believersheritagechurch.utils.Result
import com.faithdeveloper.believersheritagechurch.utils.Status
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await

class MessageRepositoryImpl(private val database: FirebaseFirestore) :
    MessagesRepository {


    override suspend fun messageSectionItems() = flow {
        val snapshot = database.collection("Message Sections").get(Source.SERVER).await()
        if (!snapshot.isEmpty) {
            emit(Result(Status.SUCCESS, formatSnapshot<MessageSectionItems>(snapshot)))
        } else {
            throw (Exception())
        }
    }.catch {
        emit(Result(Status.ERROR, emptyList()))
    }

    override suspend fun getMessages(
        messageType: String,
        loadSize: Long,
        loadType: LoadType,
        key: DocumentSnapshot?
    ): MessageResult {
        val query = if (key == null) {
            database.collection(messageType).orderBy(
                "date",
                when (loadType) {
                    LoadType.DESCENDING -> com.google.firebase.firestore.Query.Direction.DESCENDING
                    else -> com.google.firebase.firestore.Query.Direction.ASCENDING
                }
            ).limit(loadSize)
        } else {
            database.collection(messageType).orderBy(
                "date",
                when (loadType) {
                    LoadType.DESCENDING -> com.google.firebase.firestore.Query.Direction.DESCENDING
                    else -> com.google.firebase.firestore.Query.Direction.ASCENDING
                }
            ).limit(loadSize).startAfter(key)
        }

        val result = query.get(Source.SERVER).await()

        return if (result.isEmpty) MessageResult(null, listOf())
        else MessageResult(result.last(), formatSnapshot(result))
    }

}