package com.aasan.sample


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.aasan.sample.base.*
import com.aasan.sample.databinding.ActivityMainBinding

import com.globalmed.corelib.repository.SharedPreferenceRepositoryImplementation
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val header = MutableLiveData<Header>()
    private lateinit var activityMainBinding: ActivityMainBinding
    val viewModel: BaseViewModel by viewModels()
    var navController: NavController? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        activityMainBinding.frmProgress.setOnClickListener {  }
        viewModel.repository = SharedPreferenceRepositoryImplementation(application)
        setContentView(activityMainBinding.root)
     }

    fun showProgress(_shouldShowProgress: Boolean, _progressText: String?) {
        header.value?.updatedProgressOnly(_shouldShowProgress, _progressText)?.let {
            header.postValue(it)
        }
    }

    fun updateHeader(_header: Header) {
        header.postValue(_header)
        if(_header.currentFragment != 0)
            viewModel.updateDestination(_header.currentFragment)
    }
}
