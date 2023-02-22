package com.aasan.sample


import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aasan.sample.sample.SampleAdapter
import com.aasan.sample.sample.data.SampleData



object Extension {
    @JvmStatic
    @BindingAdapter("sample_list_adapter")
    fun setSampleListData(view: RecyclerView?,list: SampleData?) {
        view?.adapter?.let { adapter ->
            adapter as SampleAdapter
            adapter.updateData(list?.entries ?: emptyList())
        }
    }
}