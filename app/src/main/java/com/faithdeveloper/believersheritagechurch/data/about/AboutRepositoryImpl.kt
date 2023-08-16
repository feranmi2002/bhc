package com.faithdeveloper.believersheritagechurch.data.about

import com.faithdeveloper.believersheritagechurch.data.Util
import com.faithdeveloper.believersheritagechurch.utils.Result
import com.faithdeveloper.believersheritagechurch.utils.Status
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AboutRepositoryImpl(private val database: FirebaseFirestore) : AboutRepository {
    override fun getListOfAboutItems(): Flow<Result<AboutItem>> = flow {
        val snapshot = database.collection("AboutList").get().await()
        val aboutList = Util.formatSnapshot<AboutItem>(snapshot)
        if (aboutList.isNotEmpty()) {
            emit(Result(Status.SUCCESS, aboutList))
        } else {
            emit(Result(Status.ERROR, emptyList()))
        }
    }.catch {
        emit(Result(Status.ERROR, emptyList()))
    }
}