package com.kernacs.scooterhunter.ui.details

import androidx.lifecycle.viewModelScope
import com.kernacs.scooterhunter.base.BaseViewModel
import com.kernacs.scooterhunter.data.entity.ScooterEntity
import com.kernacs.scooterhunter.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScooterDetailsViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<ScooterEntity>() {

    fun loadVehicleData(id: String) =
        viewModelScope.launch {
            loadData {
                repository.getScooter(id)
            }
        }
}