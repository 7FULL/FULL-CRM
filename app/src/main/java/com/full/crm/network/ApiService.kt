package com.full.crm.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST

//https://developer.android.com/studio/run/emulator-networking
//Tenemos que poner 10.0.2.2 en vez de localhost porque estamos en un emulador
private val BASE_URL = "http://10.0.2.2:8080/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ApiService {

    /**
     * PIN PON
     */
    @GET("api/pin")
    suspend fun pin(): Response<String>

    /**
     * Devuelve un empleado de prueba
     */
    @GET("api/devUser")
    suspend fun devUser(): Response<String>

    /**
     * Funcion para loguearse
     * @param username
     * @param password
     * @return Devuelve un usuario en formato JSON si el login es correcto, sino devuelve un usuario vacio
     */
    @POST("api/login")
    suspend fun login(username: String, password: String): Response<String>
}

object API {
    val service : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

data class Employee(val username: String, val password: String, val name: String, val surname: String, val email: String, val phone: String)