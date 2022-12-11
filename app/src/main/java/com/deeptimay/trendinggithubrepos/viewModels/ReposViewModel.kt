package com.deeptimay.trendinggithubrepos.viewModels

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.deeptimay.trendinggithubrepos.data.model.Repo
import com.deeptimay.trendinggithubrepos.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReposViewModel @Inject constructor(
    private val repository: GithubRepository
) : ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val repos: LiveData<PagingData<Repo>> = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString)
    }.cachedIn(viewModelScope)

    fun searchRepos(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = "Q"
    }
}