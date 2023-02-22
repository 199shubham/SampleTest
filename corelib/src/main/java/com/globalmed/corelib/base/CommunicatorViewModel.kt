package com.globalmed.corelib.base

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Hemant Kumar on 9/8/2021.
 */
class CommunicatorViewModel:ViewModel() {

    private var _communicateLiveData = SingleLiveEventCommunicator<Boolean>()
    val communicateLiveData: LiveData<Boolean> = _communicateLiveData

    private var _restartAppLiveData = SingleLiveEventCommunicator<Void>()
    val restartAppLiveData: LiveData<Void> = _restartAppLiveData

    private var _updateAppLanguageLiveData = SingleLiveEventCommunicator<String>()
    val updateAppLanguageLiveData: LiveData<String> = _updateAppLanguageLiveData


    fun showLoading(){
        _communicateLiveData.postValue(true)
    }

    fun hideLoading(){
        _communicateLiveData.postValue(false)
    }

    fun restartApp(){
        _restartAppLiveData.call()
    }

    fun updateAppLanguage(language:String){
        _updateAppLanguageLiveData.postValue(language)
    }
}

class SingleLiveEventCommunicator<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }
        // Observe the internal MutableLiveData
        super.observe(owner, Observer { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }
    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
    companion object {
        private val TAG = "SingleLiveEvent"
    }
}