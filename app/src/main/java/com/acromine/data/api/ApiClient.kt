package com.acromine.data.api

import com.acromine.data.model.AcromineModel
import retrofit2.http.GET

interface ApiClient {
    @GET(ApiDetails.ENDPOINT_USER)
    suspend fun getAcromine(
        //any parameters
    ): AcromineModel //return type
}