package com.faithdeveloper.believersheritagechurch.data.home

import android.content.Context
import com.faithdeveloper.believersheritagechurch.data.Util
import com.faithdeveloper.believersheritagechurch.utils.Result
import com.faithdeveloper.believersheritagechurch.utils.Status
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class HomeRepositoryImpl(private val database: FirebaseFirestore, val context: Context) :
    HomeRepository {
    override suspend fun getImageSliderImages(): Flow<Result<ImageSliderImage>> = flow {
        val snapshot = database.collection("ImageSliderImages")
            .orderBy("date", Query.Direction.DESCENDING)
            .get().await()
        val images = Util.formatSnapshot<ImageSliderImage>(snapshot)
        if (images.isNotEmpty()) {
            emit(Result(Status.SUCCESS, images))
        } else {
            emit(Result(Status.ERROR, emptyList()))
        }
    }.catch {
        emit(Result(Status.ERROR, emptyList()))
    }
}