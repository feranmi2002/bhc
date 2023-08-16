package com.faithdeveloper.believersheritagechurch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.faithdeveloper.believersheritagechurch.data.LoadType
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.data.messages.MessagePagingSource
import com.faithdeveloper.believersheritagechurch.data.messages.MessagesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class MessageViewModel(private val repository: MessagesRepository, val messageType: String) :
    ViewModel() {

    private val _loadType = MutableStateFlow(LoadType.DESCENDING)
    val loadType = _loadType.asStateFlow()

    private val _messages: Flow<PagingData<Message>>
    val messages: Flow<PagingData<Message>>

    private val _searchText = MutableStateFlow("")
    val searchText get() = _searchText.asStateFlow()

    private val usedSearchText = MutableStateFlow("")

    init {
        _messages = _loadType
            .combine(usedSearchText) { loadType, searchText ->
//                do nothing
            }.flatMapLatest {
                if (searchText.value.isNotBlank()) {
                    Pager(
                        PagingConfig(pageSize = 30),
                        pagingSourceFactory = {
                            MessagePagingSource(messageType, repository, loadType.value, usedSearchText.value)
                        }
                    ).flow.cachedIn(viewModelScope)

                } else {
                    Pager(
                        PagingConfig(pageSize = 30),
                        pagingSourceFactory = {
                            MessagePagingSource(messageType, repository, loadType.value)
                        }
                    ).flow.cachedIn(viewModelScope)
                }
            }
        messages = _messages
    }

    fun updateLoadType(loadType: LoadType) {
        _loadType.value = loadType
    }

    fun updateSearchText(it: String) {
        _searchText.value = it
    }

    fun search() {
        usedSearchText.value = searchText.value
    }


    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            repository: MessagesRepository,
            messageType: String
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MessageViewModel(repository, messageType) as T
                }
            }
    }
}