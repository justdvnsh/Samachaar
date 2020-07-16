package divyansh.tech.kotnewreader.network.api

import divyansh.tech.kotnewreader.network.models.MLModels.ArticleView
import divyansh.tech.kotnewreader.utils.Constants.Companion.ARTICLE_API
import divyansh.tech.kotnewreader.utils.Constants.Companion.RAPID_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface MLApi {

    @GET
    suspend fun getArticleText(
        @Url url: String = ARTICLE_API,
        @Header("X-RapidAPI-Host") api: String = "text-analyzer.p.rapidapi.com",
        @Header("X-RapidAPI-Key") apiKey: String = RAPID_API_KEY,
        @Query("url") query_url: String
    ): Response<ArticleView>
}