package com.faithdeveloper.believersheritagechurch.data.about

import com.faithdeveloper.believersheritagechurch.utils.Result
import kotlinx.coroutines.flow.Flow

interface AboutRepository {
    fun getListOfAboutItems(): Flow<Result<AboutItem>>
}