package com.faithdeveloper.believersheritagechurch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.faithdeveloper.believersheritagechurch.data.about.AboutItem
import com.faithdeveloper.believersheritagechurch.data.about.AboutRepository
import com.faithdeveloper.believersheritagechurch.utils.Result
import com.faithdeveloper.believersheritagechurch.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AboutViewModel(
    private val repository: AboutRepository
) : ViewModel() {

    private val _aboutList = MutableStateFlow(com.faithdeveloper.believersheritagechurch.utils.Result<AboutItem>(Status.LOADING, emptyList()))
    val aboutList get() = _aboutList.asStateFlow()


    fun getAboutList(){
        viewModelScope.launch {
            repository.getListOfAboutItems().collectLatest {
                _aboutList.value = it
            }
        }

    }

    fun retry(){
        _aboutList.value = Result(Status.LOADING, emptyList())
        getAboutList()
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            repository: AboutRepository,
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AboutViewModel(repository) as T
                }
            }
    }
}