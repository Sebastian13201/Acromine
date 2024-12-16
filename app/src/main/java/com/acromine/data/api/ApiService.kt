package com.acromine.data.network

import com.acromine.data.model.AcromineModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("dictionary.py")
    fun getLongForms(@Query("sf") acronym: String): Call<AcromineModel>
}