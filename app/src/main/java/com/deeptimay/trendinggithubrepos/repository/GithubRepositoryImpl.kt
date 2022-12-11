package com.deeptimay.trendinggithubrepos.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.deeptimay.trendinggithubrepos.base.api.GithubApi
import com.deeptimay.trendinggithubrepos.data.GithubPagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepositoryImpl @Inject constructor(private val githubApi: GithubApi) : GithubRepository {

    override fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 1000,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GithubPagingSource(githubApi, query) }
        ).liveData
}