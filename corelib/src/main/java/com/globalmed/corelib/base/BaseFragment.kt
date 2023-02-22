package com.globalmed.corelib.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

abstract class BaseFragment<in BaseViewModel> : Fragment(){

    lateinit var communicatorViewModel: CommunicatorViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicatorViewModel = ViewModelProvider(requireActivity()).get(CommunicatorViewModel::class.java)
    }

}