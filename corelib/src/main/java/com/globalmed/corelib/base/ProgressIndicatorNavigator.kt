package com.globalmed.corelib.base


interface ProgressIndicatorNavigator: MarkerBaseNavigator {
    fun showProgress()
    fun hideProgress()
    fun showError(error: String)
    fun showError(error: Int)
    fun showSuccess(message: String)
    fun showSuccess(message: Int)

}