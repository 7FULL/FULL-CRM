package com.full.crm.network

import com.full.crm.models.Employee
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    /**
     * PIN PON
     */
    @GET("api/pin")
    suspend fun pin(): Response<DataResponse<String>>

    /**
     * Devuelve un empleado de prueba
     */
    @GET("api/employee/devUser")
    suspend fun devUser(): Response<DataResponse<Employee>>

    /**
     * Funcion para loguearse
     * @param username
     * @param password
     * @return Devuelve un usuario si el login es correcto, sino devuelve un usuario vacio
     */
    @POST("api/employee/login")
    suspend fun login(@Query("username") username: String, @Query("password") password: String): Response<DataResponse<Employee>>

    /**
     * Funcion para obtener el usuario basado en el email
     * @param email
     * @return Devuelve un usuario si el login es correcto, sino devuelve un usuario vacio
     */
    @GET("api/employee")
    suspend fun getByEmail(@Query("email") email: String): Response<DataResponse<Employee>>
}