package divyansh.tech.kotnewreader.network.api

import divyansh.tech.kotnewreader.network.models.NewsResponse
import divyansh.tech.kotnewreader.utils.Constants.Companion.API_KEY
import divyansh.tech.kotnewreader.utils.Constants.Companion.CATEGORY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "in",
        @Query("category")
        category: String = CATEGORY,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("/v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery: String,
        @Query("category")
        category: String = CATEGORY,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>
}