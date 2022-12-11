package com.deeptimay.trendinggithubrepos.base.api

import com.deeptimay.trendinggithubrepos.data.model.TrendingRepoResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubApi {

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }

    @Headers(
        "Accept: application/vnd.github+json",
        "Authorization: Bearer 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",
        "X-GitHub-Api-Version: 2022-11-28"
    )
    @GET("search/repositories?sort=stars")
    suspend fun getTrendingRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): TrendingRepoResponse
}