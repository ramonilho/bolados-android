package br.com.ramonilho.bolados.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by ramonhonorio on 22/07/17.
 */

object RetrofitClient {
    private var retrofit: Retrofit? = null
    private var retrofitExternal: Retrofit? = null

    val logging = HttpLoggingInterceptor()

    fun getClient(baseUrl: String): Retrofit? {
        if (retrofit == null)
            generateClient(false, baseUrl)
        return retrofit
    }

    fun getExternalClient(baseUrl: String): Retrofit? {
        if (retrofitExternal == null)
            generateClient(true, baseUrl)
        return retrofitExternal
    }

    private fun generateClient(isExternal: Boolean, baseUrl: String) {
        // HTTP Logging Level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .addInterceptor(logging)
                .build()

        if (isExternal) {
            retrofitExternal = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        } else {
            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

    }
}