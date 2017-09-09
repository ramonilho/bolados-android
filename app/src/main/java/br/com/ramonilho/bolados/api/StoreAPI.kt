package br.com.ramonilho.bolados.api

import br.com.ramonilho.bolados.model.Store
import br.com.ramonilho.bolados.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Created by ramonhonorio on 22/07/17.
 */

interface StoreAPI {

    @get:GET("/stores")
    val stores: Call<List<Store>>

    @GET("/stores/{id}")
    fun store(@Path("id") id: Int): Call<Store>

    @PUT("/store")
    fun editInfo(@Body Store: Store) : Call<Store>
}