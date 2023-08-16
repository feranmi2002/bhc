package com.faithdeveloper.believersheritagechurch.data.home

import kotlinx.coroutines.flow.Flow
import com.faithdeveloper.believersheritagechurch.utils.Result

interface HomeRepository {

    suspend fun getImageSliderImages(): Flow<Result<ImageSliderImage>>

}