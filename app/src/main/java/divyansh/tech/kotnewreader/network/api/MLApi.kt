package divyansh.tech.kotnewreader.network.api

import divyansh.tech.kotnewreader.network.models.MLModels.*
import divyansh.tech.kotnewreader.utils.Constants.Companion.ARTICLE_API
import divyansh.tech.kotnewreader.utils.Constants.Companion.RAPID_API_KEY
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface MLApi {

    @GET
    suspend fun getArticleText(
        @Url url: String = ARTICLE_API,
        @Header("X-RapidAPI-Host") api: String = "text-analyzer.p.rapidapi.com",
        @Header("X-RapidAPI-Key") apiKey: String = RAPID_API_KEY,
        @Query("url") query_url: String
    ): Response<ArticleView>

    @GET
    suspend fun getTranslation(
        @Url url: String = "https://language-translation.p.rapidapi.com/translateLanguage/translate",
        @Header("X-RapidAPI-Host") api: String = "language-translation.p.rapidapi.com",
        @Header("X-RapidAPI-Key") apiKey: String = RAPID_API_KEY,
        @Query("text") query_text: String,
        @Query("type") type: String = "plain",
        @Query("target") target: String = "hi"
    ) : Response<translationModel>

    @FormUrlEncoded
    @POST
    suspend fun getSentiments(
        @Url url: String = "https://text-sentiment.p.rapidapi.com/analyze",
        @Header("X-RapidAPI-Host") api: String = "text-sentiment.p.rapidapi.com",
        @Header("X-RapidAPI-Key") apiKey: String = RAPID_API_KEY,
        @Header("content-type") type: String = "application/x-www-form-urlencoded",
        @Field("text") body: String
    ): Response<sentimentModel>

    @POST
    suspend fun getCommunicationAnalysis(
        @Url url: String = "https://text-analytics-by-symanto.p.rapidapi.com/communication",
        @Header("X-RapidAPI-Host") api: String = "text-analytics-by-symanto.p.rapidapi.com",
        @Header("X-RapidAPI-Key") apiKey: String = RAPID_API_KEY,
        @Header("content-type") type: String = "application/json",
        @Header("accept") accept: String = "application/json",
        @Body request: RequestBody
    ): Response<List<communicationAnalysis>>

    @POST
    suspend fun getEmotions(
        @Url url: String = "https://text-analytics-by-symanto.p.rapidapi.com/emotion",
        @Header("X-RapidAPI-Host") api: String = "text-analytics-by-symanto.p.rapidapi.com",
        @Header("X-RapidAPI-Key") apiKey: String = RAPID_API_KEY,
        @Header("content-type") type: String = "application/json",
        @Header("accept") accept: String = "application/json",
        @Body request: RequestBody
    ): Response<List<communicationAnalysis>>

    @POST
    suspend fun getKeyPhrases(
        @Url url: String = "https://microsoft-text-analytics1.p.rapidapi.com/keyPhrases",
        @Header("X-RapidAPI-Host") api: String = "microsoft-text-analytics1.p.rapidapi.com",
        @Header("X-RapidAPI-Key") apiKey: String = RAPID_API_KEY,
        @Header("content-type") type: String = "application/json",
        @Header("accept") accept: String = "application/json",
        @Body request: RequestBody
    ) : Response<keyPhrasesModel>

    @POST
    suspend fun getEntities(
        @Url url: String = "https://microsoft-text-analytics1.p.rapidapi.com/entities/recognition/general",
        @Header("X-RapidAPI-Host") api: String = "microsoft-text-analytics1.p.rapidapi.com",
        @Header("X-RapidAPI-Key") apiKey: String = RAPID_API_KEY,
        @Header("content-type") type: String = "application/json",
        @Header("accept") accept: String = "application/json",
        @Body request: RequestBody
    ) : Response<entityModel>

    @GET
    suspend fun getRelatedNews(
        @Url url: String,
        @Header("X-RapidAPI-Host") api: String = "google-search3.p.rapidapi.com",
        @Header("X-RapidAPI-Key") apiKey: String = RAPID_API_KEY
    ) : Response<relatedNews>
}