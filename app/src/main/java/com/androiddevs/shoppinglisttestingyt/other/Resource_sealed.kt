package com.androiddevs.shoppinglisttestingyt.other

sealed class Resource_sealed <T>(
    val data:T?=null,
    val message:String?=null
){
    class Success<T>(data: T?):Resource_sealed<T>(data)
    class Error<T>(message: String,data:T?=null):Resource_sealed<T>(data, message)
    class Loading<T>:Resource_sealed<T>()
}