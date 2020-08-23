package divyansh.tech.kotnewreader.network.api

import divyansh.tech.kotnewreader.models.Corona
import retrofit2.Response
import retrofit2.http.*

interface CoronaApi {

    @GET
    suspend fun getDailyReport(
        @Url url: String
    ): Response<Corona>
}