package com.faithdeveloper.believersheritagechurch.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.faithdeveloper.believersheritagechurch.data.home.HomeRepository
import com.faithdeveloper.believersheritagechurch.data.home.ImageSliderImage
import com.faithdeveloper.believersheritagechurch.utils.Result
import com.faithdeveloper.believersheritagechurch.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    private val _imageSliderImages =
        MutableStateFlow(Result<ImageSliderImage>(Status.LOADING, emptyList()))

    val imageSliderImages get() = _imageSliderImages.asStateFlow()


    fun getImageSliderImages() {
        viewModelScope.launch {
            homeRepository.getImageSliderImages().collectLatest {
                _imageSliderImages.value = it
            }
        }
    }

    fun retry() {
        _imageSliderImages.value = Result(Status.LOADING, emptyList())
        getImageSliderImages()
    }


    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(homeRepository: HomeRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(homeRepository) as T
                }
            }
    }
}