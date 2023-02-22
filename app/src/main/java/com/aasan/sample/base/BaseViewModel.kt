package com.aasan.sample.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aasan.sample.AppConstants
import com.globalmed.corelib.repository.SharedPreferenceRepository

open class BaseViewModel(application: Application): AndroidViewModel(application){
    lateinit var repository: SharedPreferenceRepository
    fun updateDestination(id: Int){
        if(this::repository.isInitialized){
            repository.writeIntToPreference(AppConstants.APP_DESTINATION, id)
        }
    }


}