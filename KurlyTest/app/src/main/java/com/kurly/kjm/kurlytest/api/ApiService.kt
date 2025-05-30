package com.kurly.kjm.kurlytest.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.kurly.android.mockserver.MockInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiService(private val context: Context) {
    private fun okHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(MockInterceptor(context))
            .build()

    private fun gsonFactory(): GsonConverterFactory = GsonConverterFactory.create(GsonBuilder().setLenient().create())

    private val clientRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://kurly.com/")
            .client(okHttpClient())
            .addConverterFactory(gsonFactory())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    val api: KurlyApi by lazy {
        clientRetrofit.create(KurlyApi::class.java)
    }
}