package com.altunoymak.esarj.util

sealed class Response<out T>(val data : T?,val message : String?, val status : ResponseStatus?){
    data class Success<T>(val _data : T) : Response<T>(
        data = _data,
        message = null,
        status = ResponseStatus.SUCCESS
    )
    data class Error(val _message : String) : Response<Nothing>(
        data = null,
        message = _message,
        status = ResponseStatus.ERROR
    )
    data object Loading : Response<Nothing>(
        data = null,
        message = null,
        status = ResponseStatus.LOADING
    )
}

enum class ResponseStatus {
    LOADING,
    SUCCESS,
    ERROR
}