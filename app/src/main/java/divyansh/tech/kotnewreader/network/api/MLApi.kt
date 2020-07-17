package divyansh.tech.kotnewreader.network.api

import divyansh.tech.kotnewreader.network.models.MLModels.ArticleView
import divyansh.tech.kotnewreader.network.models.MLModels.translationModel
import divyansh.tech.kotnewreader.utils.Constants.Companion.ARTICLE_API
import divyansh.tech.kotnewreader.utils.Constants.Companion.RAPID_API_KEY
import divyansh.tech.kotnewreader.utils.Constants.Companion.TRANSLATION_API
import org.json.JSONObject
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
}