package com.faithdeveloper.believersheritagechurch.data.programmes

import com.faithdeveloper.believersheritagechurch.data.Util
import com.faithdeveloper.believersheritagechurch.utils.Result
import com.faithdeveloper.believersheritagechurch.utils.Status
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await

class ProgrammesRepositoryImpl(
    private val database: FirebaseFirestore
) : ProgrammesRepository {

    override suspend fun getProgrammes() = flow {
        val snapshot = database.collection("Programmes").get(Source.SERVER).await()
        if (!snapshot.isEmpty) {
            emit(Result(Status.SUCCESS, Util.formatSnapshot<Programme>(snapshot)))
        } else {
            throw (Exception())
        }
    }.catch {
        emit(Result(Status.ERROR, emptyList()))
    }

    companion object {
        const val PROGRAMMES_ID = "programmes_id"
    }
}