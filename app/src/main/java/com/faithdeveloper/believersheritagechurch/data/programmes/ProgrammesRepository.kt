package com.faithdeveloper.believersheritagechurch.data.programmes

import com.faithdeveloper.believersheritagechurch.utils.Result
import kotlinx.coroutines.flow.Flow

interface ProgrammesRepository {
    suspend fun getProgrammes(): Flow<Result<Programme>>
}