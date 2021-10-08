package com.jhonatanrojas.searchapp.di.network

import com.jhonatanrojas.searchapp.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by JHONATAN ROJAS on 8/10/2021.
 */
class NetworkClient {

    private fun createOkHttpClient(): OkHttpClient {

        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .build()
                chain.proceed(request)
            }

        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }

        return okHttpClientBuilder.build()
    }

    fun getRetrofitInstance(baseUrl: String): Retrofit {
        val moshi: Moshi = Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createOkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    companion object {
        private const val TIME_OUT: Long = 30
    }
}