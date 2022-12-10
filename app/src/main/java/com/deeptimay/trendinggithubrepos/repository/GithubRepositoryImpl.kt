package com.deeptimay.trendinggithubrepos.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.deeptimay.trendinggithubrepos.data.GithubPagingSource
import com.deeptimay.trendinggithubrepos.base.api.GithubApi

class GithubRepositoryImpl  constructor(private val githubApi: GithubApi): GithubRepository {

    override fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GithubPagingSource(githubApi, query) }
        ).liveData
}