package br.com.ramonilho.bolados.api

import br.com.ramonilho.bolados.model.User
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by ramonhonorio on 19/08/17.
 */
interface UserAPI {

    @GET("/users")
    fun authenticate(@Header("email") email: String, @Header("password") password: String) : Call<User>

    @GET("/users/u/{id}")
    fun getUser(@Path("id") id: Int) : Call<User>

    @PUT("/users")
    fun editInfo(@Body user: User) : Call<User>

    @GET("/users/mocked")
    fun mockedUser() : Call<User>

    @POST("/users")
    fun signup(@Body user: User) : Call<User>

}