package com.kernacs.scooterhunter.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kernacs.scooterhunter.util.EmptyDataException
import com.kernacs.scooterhunter.util.Result

abstract class BaseViewModel<T> : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private val _error = MutableLiveData<Throwable?>()
    val error: LiveData<Throwable?>
        get() = _error
    private val _data = MutableLiveData<T>()
    val data: LiveData<T>
        get() = _data

    suspend fun loadData(getData: suspend () -> Result<T>) {
        Log.d(TAG, "Attempt to load data")
        _isLoading.value = true
        when (val result = getData()) {
            is Result.Success -> {
                _isLoading.value = false
                result.data?.let {
                    _data.value = it
                    Log.d(TAG, "Loading of the data is successful")
                    _error.value = null
                } ?: run {
                    Log.d(TAG, "Loaded data is null!")
                    _error.value = EmptyDataException()
                }
            }
            is Result.Error -> {
                Log.d(TAG, "Error during data refresh: ${result.exception}")
                _isLoading.value = false
                _error.value = result.exception
            }
            is Result.Loading -> _isLoading.postValue(true)
        }
    }

    companion object {
        private val TAG = BaseViewModel::class.java.simpleName
    }
}