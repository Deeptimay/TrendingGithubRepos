package com.deeptimay.trendinggithubrepos.activities

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ItemSnapshotList
import androidx.paging.LoadState
import com.deeptimay.trendinggithubrepos.R
import com.deeptimay.trendinggithubrepos.R.layout.activity_trending_repository
import com.deeptimay.trendinggithubrepos.adapter.ReposAdapter
import com.deeptimay.trendinggithubrepos.adapter.ReposLoadStateAdapter
import com.deeptimay.trendinggithubrepos.data.model.Languages
import com.deeptimay.trendinggithubrepos.data.model.Repo
import com.deeptimay.trendinggithubrepos.databinding.ActivityTrendingRepositoryBinding
import com.deeptimay.trendinggithubrepos.util.hide
import com.deeptimay.trendinggithubrepos.util.show
import com.deeptimay.trendinggithubrepos.viewModels.ReposViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TrendingRepoSearchActivity : AppCompatActivity() {

    private lateinit var viewModel: ReposViewModel
    private lateinit var binding: ActivityTrendingRepositoryBinding
    lateinit var reposAdapter: ReposAdapter
    private var repoList: ItemSnapshotList<Repo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, activity_trending_repository)
        setContentView(binding.root)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this).get(ReposViewModel::class.java)

        displayLoadingState()

        reposAdapter = ReposAdapter()
        binding.apply {
            binding.rvRepository.apply {
                setHasFixedSize(true)
                itemAnimator = null
                this.adapter = reposAdapter.withLoadStateHeaderAndFooter(
                    header = ReposLoadStateAdapter { reposAdapter.retry() },
                    footer = ReposLoadStateAdapter { reposAdapter.retry() }
                )
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }

            binding.layoutError.lookUpButton.setOnClickListener {
                reposAdapter.retry()
            }
        }

        viewModel.repos.observe(this) {
            reposAdapter.submitData(this.lifecycle, it)
        }

        reposAdapter.addLoadStateListener { loadState ->
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

                // no results found
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    reposAdapter.itemCount < 1
                ) {
//                    displayErrorState()
                } else {
//                    hideLoadingState()
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_repos, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        val searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete

        // Get SearchView autocomplete object
        searchAutoComplete.setTextColor(Color.BLACK)
        searchAutoComplete.setDropDownBackgroundResource(R.color.colorSecondary)

        val newsAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.dropdown_item,
            Languages.data
        )
        searchAutoComplete.setAdapter(newsAdapter)

        // Listen to search view item on click event
        searchAutoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, itemIndex, _ ->
                val queryString = adapterView.getItemAtPosition(itemIndex) as String
                Toast.makeText(this, "search: ", Toast.LENGTH_SHORT).show()
                searchAutoComplete.setText(String.format(getString(R.string.search_query), queryString))
                binding.rvRepository.scrollToPosition(0)
                val languageQuery = String.format(getString(R.string.query), queryString)
                viewModel.searchRepos(languageQuery)

//                repoList = reposAdapter.snapshot()
//
//                reposAdapter.submitData(this.lifecycle, repoList.)
                searchView.clearFocus()
                (this as AppCompatActivity).supportActionBar?.title = queryString.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            }
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                // Action goes here
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        binding.loadingLayout.containerShimmer.startShimmer()
    }

    private fun hideLoadingState() {
        binding.layoutError.clErrorMain.hide()
        binding.rvRepository.show()
        binding.loadingLayout.containerShimmer.hide()
        binding.loadingLayout.containerShimmer.stopShimmer()
    }
}
