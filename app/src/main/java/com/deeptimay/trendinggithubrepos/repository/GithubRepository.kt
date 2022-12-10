package com.deeptimay.trendinggithubrepos.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.deeptimay.trendinggithubrepos.data.model.Repo

interface GithubRepository {
    fun getSearchResults(query: String):LiveData<PagingData<Repo>>
}