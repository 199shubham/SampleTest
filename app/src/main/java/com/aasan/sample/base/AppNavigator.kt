package com.aasan.sample.base



sealed interface AppNavigator{
    fun showError(_errorMsg: String)
    fun showProgress(_shouldShowProgress: Boolean, _progressText: String?)
    fun showToastMessage(msg: String)
    fun showDialogMessage(msg: String)



    interface SampleDetailsNavigator: AppNavigator{
        fun onContinue()


    }























}