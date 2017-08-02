package br.com.ramonilho.bolados.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by ramonhonorio on 22/07/17.
 */

object APIUtils {

    val BASE_URL = "http://10.0.0.105:3000"

    val storeAPIVersion: StoreAPI
        get() = RetrofitClient.getClient(BASE_URL)!!.create(StoreAPI::class.java)

}