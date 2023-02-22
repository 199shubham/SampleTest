package com.aasan.sample.sample

import android.os.Bundle

import android.view.View

import androidx.fragment.app.activityViewModels

import androidx.recyclerview.widget.LinearLayoutManager
import com.aasan.sample.Header
import com.aasan.sample.MainActivity
import com.aasan.sample.R


import com.aasan.sample.base.AppNavigator
import com.aasan.sample.base.BaseFragment
import com.aasan.sample.databinding.FragmentSampleDetailsBinding


import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleDetailsFragment  : BaseFragment<SampleViewModel>(), AppNavigator.SampleDetailsNavigator{

    override var layoutId: Int = R.layout.fragment_sample_details
    val viewModel: SampleViewModel by activityViewModels()
    private lateinit var sampleAdapter: SampleAdapter

    val sampleBinding: FragmentSampleDetailsBinding by lazy {
        binding as FragmentSampleDetailsBinding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).updateHeader(
            Header((requireActivity() as MainActivity),
                true, "Select", false, true, currentFragment = R.id.sampleDetailsFragment)
        )
        viewModel.navigator = this
        sampleBinding.vm = viewModel
        sampleAdapter = SampleAdapter(viewModel)
        sampleBinding.recSample.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        sampleBinding.recSample.adapter = sampleAdapter
        viewModel.getSampleData()

    }

    override fun onContinue() {
        //addressBinding.root.findNavController().navigate(AddressDetailsFragmentDirections.actionAddressDetailsFragmentToHomeFragment())
    }








}