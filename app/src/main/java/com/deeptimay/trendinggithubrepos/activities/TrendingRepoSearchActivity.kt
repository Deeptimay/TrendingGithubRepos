package com.deeptimay.trendinggithubrepos.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import com.deeptimay.trendinggithubrepos.R.layout.activity_trending_repository
import com.deeptimay.trendinggithubrepos.adapter.ReposAdapter
import com.deeptimay.trendinggithubrepos.adapter.ReposLoadStateAdapter
import com.deeptimay.trendinggithubrepos.databinding.ActivityTrendingRepositoryBinding
import com.deeptimay.trendinggithubrepos.util.hide
import com.deeptimay.trendinggithubrepos.util.show
import com.deeptimay.trendinggithubrepos.viewModels.ReposViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrendingRepoSearchActivity : AppCompatActivity() {

    private val viewModel by viewModels<ReposViewModel>()
    lateinit var binding: ActivityTrendingRepositoryBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        binding = DataBindingUtil.setContentView(this, activity_trending_repository)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = ReposAdapter()

        binding.apply {

            rvRepository.apply {
                setHasFixedSize(true)
                itemAnimator = null
                this.adapter = adapter.withLoadStateHeaderAndFooter(
                    header = ReposLoadStateAdapter { adapter.retry() },
                    footer = ReposLoadStateAdapter { adapter.retry() }
                )
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }

//            btnRetry.setOnClickListener {
//                adapter.retry()
//            }
        }

        viewModel.repos.observe(this) {
            adapter.submitData(this.lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                when (loadState.source.refresh) {
                    is LoadState.Loading -> {
                        displayLoadingState()
                    }
                    is LoadState.NotLoading -> {
                        hideLoadingState()
                    }
                    is LoadState.Error -> {
                        displayErrorState()
                    }
                }
//                progress.isVisible = loadState.source.refresh is LoadState.Loading
//                recycler.isVisible = loadState.source.refresh is LoadState.NotLoading
//                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
//                error.isVisible = loadState.source.refresh is LoadState.Error

                // no results found
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
//                    recycler.isVisible = false
//                    emptyTv.isVisible = true
                    displayErrorState()
                } else {
//                    emptyTv.isVisible = false
                    hideLoadingState()
                }
            }
        }

//        setHasOptionsMenu(true)
    }


    private fun displayErrorState() {
        binding.layoutError.clErrorMain.show()
        binding.rvRepository.hide()
        binding.loadingLayout.containerShimmer.hide()
        binding.loadingLayout.containerShimmer.stopShimmer()
    }

    private fun displayLoadingState() {
        binding.layoutError.clErrorMain.hide()
        binding.rvRepository.hide()
        binding.loadingLayout.containerShimmer.show()
        binding.loadingLayout.containerShimmer.showShimmer(true)
    }

    private fun hideLoadingState() {
        binding.layoutError.clErrorMain.hide()
        binding.rvRepository.show()
        binding.loadingLayout.containerShimmer.hide()
        binding.loadingLayout.containerShimmer.stopShimmer()
    }

}
