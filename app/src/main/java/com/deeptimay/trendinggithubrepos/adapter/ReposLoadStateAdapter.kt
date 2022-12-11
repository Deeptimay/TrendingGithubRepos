package com.deeptimay.trendinggithubrepos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.deeptimay.trendinggithubrepos.databinding.LayoutStatusLoadingWithErrorBinding
import com.deeptimay.trendinggithubrepos.util.hide
import com.deeptimay.trendinggithubrepos.util.show

class ReposLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<ReposLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LayoutStatusLoadingWithErrorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: LayoutStatusLoadingWithErrorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.lookUpButton.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                if (loadState is LoadState.Error) {
                    binding.lookUpButton.show()
                    binding.loadingLayout.containerShimmer.hide()
                    binding.loadingLayout.containerShimmer.stopShimmer()
                } else if (loadState is LoadState.Loading) {
                    binding.lookUpButton.hide()
                    binding.loadingLayout.containerShimmer.show()
                    binding.loadingLayout.containerShimmer.showShimmer(true)
                } else {
                    binding.lookUpButton.hide()
                    binding.loadingLayout.containerShimmer.hide()
                    binding.loadingLayout.containerShimmer.stopShimmer()
                }
            }
        }
    }
}