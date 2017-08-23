package br.com.ramonilho.bolados.api

import br.com.ramonilho.bolados.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Created by ramonhonorio on 19/08/17.
 */
interface UserAPI {

    @GET("/users")
    fun authenticate(@Header("email") email: String, @Header("password") password: String) : Call<User>

}