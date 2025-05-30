package com.kurly.kjm.kurlytest.api

import com.kurly.kjm.kurlytest.data.SectionProductResponse
import com.kurly.kjm.kurlytest.data.SectionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KurlyApi {
    @GET("sections")
    suspend fun loadPage(
        @Query("page") page: Int = 1
    ): SectionResponse

    @GET("section/products")
    suspend fun getProducts(
        @Query("sectionId") sectionId: Int = 0
    ): SectionProductResponse
}