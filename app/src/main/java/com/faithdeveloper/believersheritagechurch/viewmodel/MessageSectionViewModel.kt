package com.faithdeveloper.believersheritagechurch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.faithdeveloper.believersheritagechurch.data.messages.MessagesRepository
import com.faithdeveloper.believersheritagechurch.data.messages_section.MessageSectionItems
import com.faithdeveloper.believersheritagechurch.utils.Result
import com.faithdeveloper.believersheritagechurch.utils.Status
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MessageSectionViewModel(private val repository: MessagesRepository) : ViewModel() {

    private val _messageSections = MutableStateFlow(Result<MessageSectionItems>(Status.LOADING, emptyList()))
    val messageSections get() = _messageSections.asStateFlow()


    fun getMessageSections() {
        viewModelScope.launch {
            repository.messageSectionItems().collectLatest {
                _messageSections.value = it
            }
        }
    }

    fun retry() {
        _messageSections.value = Result(Status.LOADING, emptyList())
        getMessageSections()
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(repository: MessagesRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MessageSectionViewModel(repository) as T
                }
            }
    }
}