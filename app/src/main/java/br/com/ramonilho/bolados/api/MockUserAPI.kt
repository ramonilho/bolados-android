package br.com.ramonilho.bolados.api

import br.com.ramonilho.bolados.model.MockUser
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by ramonhonorio on 09/09/17.
 */
interface MockUserAPI {

    @GET("/v2/58b9b1740f0000b614f09d2f")
    fun checkMocked() : Call<MockUser>

}