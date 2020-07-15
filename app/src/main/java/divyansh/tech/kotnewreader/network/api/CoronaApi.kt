package divyansh.tech.kotnewreader.network.api

import divyansh.tech.kotnewreader.network.models.Corona
import divyansh.tech.kotnewreader.utils.Constants.Companion.CORONA_API_KEY
import retrofit2.Response
import retrofit2.http.*

interface CoronaApi {

    @GET
    suspend fun getDailyReport(
        @Url url: String
    ): Response<Corona>
}