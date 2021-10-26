package com.kernacs.scooterhunter.ui.map

import androidx.lifecycle.viewModelScope
import com.kernacs.scooterhunter.base.BaseViewModel
import com.kernacs.scooterhunter.data.entity.ScooterEntity
import com.kernacs.scooterhunter.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScootersViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<List<ScooterEntity>>() {

    fun refreshData() {
        viewModelScope.launch {
            load()
        }
    }

    fun load() = viewModelScope.launch { loadData { repository.getScooters() } }
}