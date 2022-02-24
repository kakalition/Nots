package com.daggery.data.common

sealed class DbResult<out T> {
    data class Success<T>(val data: T?) : DbResult<T>()
    data class Loading<T>(val data: T? = null) : DbResult<T>()
    data class Error<T>(val exception: Exception) : DbResult<T>()
}