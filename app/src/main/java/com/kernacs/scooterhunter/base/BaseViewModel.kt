package com.kernacs.scooterhunter.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kernacs.scooterhunter.util.EmptyDataException
import com.kernacs.scooterhunter.util.Result

abstract class BaseViewModel<T> : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    var error = MutableLiveData<Throwable?>()
    val data = MutableLiveData<T>()

    suspend fun loadData(getData: suspend () -> Result<T>) {
        Log.d(TAG, "Attempt to load data")
        isLoading.value = true
        when (val result = getData()) {
            is Result.Success -> {
                isLoading.value = false
                result.data?.let {
                    data.value = it
                    Log.d(TAG, "Loading of the data is successful")
                    error.value = null
                } ?: run {
                    Log.d(TAG, "Loaded data is null!")
                    error.value = EmptyDataException()
                }
            }
            is Result.Error -> {
                Log.d(TAG, "Error during data refresh: ${result.exception}")
                isLoading.value = false
                error.value = result.exception
            }
            is Result.Loading -> isLoading.postValue(true)
        }
    }

    companion object {
        private val TAG = BaseViewModel::class.java.simpleName
    }
}