package com.faithdeveloper.believersheritagechurch.utils

data class Result<T>(
    val type:Status,
    val data: List<T>
)

enum class Status{
    LOADING,
    ERROR,
    SUCCESS
}