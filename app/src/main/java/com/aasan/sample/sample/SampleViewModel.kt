package com.aasan.sample.sample


import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.aasan.sample.sample.data.*
import com.aasan.sample.sample.domain.SampleUseCase
import com.aasan.sample.base.AppNavigator
import com.aasan.sample.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(
    val app: Application,
    val useCase: SampleUseCase
) : BaseViewModel(app) {
    lateinit var navigator: AppNavigator.SampleDetailsNavigator
    val dummyDataSet = ObservableField<SampleData>()

    fun getSampleData() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                useCase.getSampleData()
            }.onSuccess {
                withContext(Dispatchers.Main){
                    if(it.isSuccessful == true){
                        dummyDataSet.set(SampleData(it.body()!!.entries))

                    }else{
                        navigator.showError(it?.message()?:"")
                    }
                }
            }.onFailure {
                withContext(Dispatchers.Main){
                    navigator.showProgress(false, null)
                    navigator.showError(it.message?:"")
                }
            }
        }
    }
}