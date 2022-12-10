package com.deeptimay.data.api

import retrofit2.http.GET


interface TrendingRepoApiService {
    @GET("repositories")
    suspend fun searchTrendingRepositories(): MutableList<TrendingRepoResponse>
}