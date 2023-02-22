package com.aasan.sample

data class Header(
    val activity: MainActivity,
    val shouldShowBackButton: Boolean = true,
    val titleText: String = "Sample",
    val shouldShowProgress: Boolean = true,
    val shouldShowToolbar: Boolean = true,
    val progressText: String? = null,
    val currentFragment: Int,
    val onBackPressedCallBack: () -> Unit = {
        activity.onBackPressed()
    }
) {
    fun updatedProgressOnly(_shouldShowProgress: Boolean, _progressText: String?): Header {
        return Header(
            activity,
            shouldShowBackButton,
            titleText,
            _shouldShowProgress,
            shouldShowToolbar,
            _progressText,
            currentFragment,
            onBackPressedCallBack
        )
    }


}
