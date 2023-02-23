package com.aasan.sample.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


import com.aasan.sample.MainActivity

import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<VM : BaseViewModel>() : Fragment(), AppNavigator {
    var binding: ViewDataBinding? = null
    abstract var layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun showError(_errorMsg: String) {
        binding?.root?.let {
            Snackbar.make(it, _errorMsg, Snackbar.LENGTH_LONG)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
        }
    }

    override fun showProgress(_shouldShowProgress: Boolean, _progressText: String?) {
        (requireActivity() as MainActivity).showProgress(_shouldShowProgress, _progressText)
    }

    override fun showToastMessage(msg: String) {

    }
    override fun showDialogMessage(msg: String) {

    }
}
