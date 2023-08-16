package com.faithdeveloper.believersheritagechurch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.faithdeveloper.believersheritagechurch.data.programmes.Programme
import com.faithdeveloper.believersheritagechurch.data.programmes.ProgrammesRepository
import com.faithdeveloper.believersheritagechurch.utils.Result
import com.faithdeveloper.believersheritagechurch.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProgrammesViewModel(
    private val repository: ProgrammesRepository
) : ViewModel() {


    private val _programmes = MutableStateFlow(Result<Programme>(Status.LOADING, emptyList()))
    val programmes get() = _programmes.asStateFlow()


    fun getProgrammes() {
        viewModelScope.launch {
            repository.getProgrammes().collectLatest {
                _programmes.value = it
            }
        }
    }

    fun retry() {
        _programmes.value = Result(Status.LOADING, emptyList())
        getProgrammes()
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            repository: ProgrammesRepository,
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ProgrammesViewModel(repository) as T
                }
            }
    }
}