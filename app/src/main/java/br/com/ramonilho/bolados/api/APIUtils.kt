package br.com.ramonilho.bolados.api

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by ramonhonorio on 22/07/17.
 */

object APIUtils {

    val MOCK_URL = "http://www.mocky.io/"

    // Home IP
//    val BASE_URL = "http://10.0.0.106:3000"

    // Heroku - External
    val BASE_URL = "https://whispering-reaches-67306.herokuapp.com"

    // FIAP random IPs
//    val BASE_URL = "http://172.16.71.218:3000"

    val storeAPIVersion: StoreAPI
        get() = RetrofitClient.getClient(BASE_URL)!!.create(StoreAPI::class.java)

    val userAPIVersion: UserAPI
        get() = RetrofitClient.getClient(BASE_URL)!!.create(UserAPI::class.java)

    val mockAPIVersion: MockUserAPI
        get() = RetrofitClient.getExternalClient(MOCK_URL)!!.create(MockUserAPI::class.java)
}