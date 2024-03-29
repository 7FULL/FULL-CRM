package com.full.crm.network

import com.full.crm.models.Appointment
import com.full.crm.models.Bill
import com.full.crm.models.Client
import com.full.crm.models.Employee
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {

    /**
     * Tipical function to check if the api is up
     * @return It returns "PON"
     */
    @GET("api/pin")
    suspend fun pin(): Response<DataResponse<String>>

    /**
     * @return Development employee
     */
    @GET("api/employee/devUser")
    suspend fun devUser(): Response<DataResponse<Employee>>

    /**
     * Function to login and get the data for that user
     * @param username The username to log in with
     * @param password The password to log in with
     * @return The employee and status 200 if the login is correct, else returns an empty employee and status 401
     */
    @POST("api/employee/login")
    suspend fun login(@Body credentials: Map<String, String> ): Response<DataResponse<Employee>>

    @POST("api/employee/getEmployee")
    suspend fun getEmployee(@Query("username") username: String): Response<DataResponse<Employee>>

    /**
     * Function to get a client based on the email
     * @param email The email to search for
     * @return The client and status 200 if the client exists, else returns an empty client and status 404
     */
    @GET("api/employee")
    suspend fun getByEmail(@Query("email") email: String): Response<DataResponse<Employee>>

    /**
     * Function to add a bill
     * @param billRequest A bill request object with the data to add
     * @return The id of the bill and status 200 if the bill is added, else returns an empty string and status 400
     */
    @POST("api/bill/addBill")
    suspend fun addBill(@Body billRequest: BillRequest): Response<DataResponse<String>>

    /**
     * Funcion para enviar el email de recuperacion de contraseña
     * @param email
     * @return Devuelve un 200
     */
    @POST("api/employee/forgotPassword")
    suspend fun forgotPassword(@Query("email") email: String): Response<DataResponse<String>>

    @POST("api/employee/checkToken")
    suspend fun checkToken(@Query("token") token: String): Response<DataResponse<String>>

    @PUT("api/employee/newPassword")
    suspend fun newPassword(@Query("token") token: String, @Query("password") password: String): Response<DataResponse<String>>

    @GET("api/admin/getBills")
    suspend fun getBills(): Response<DataResponse<Array<Bill>>>

    @GET("api/admin/getClients")
    suspend fun getClients(): Response<DataResponse<Array<Client>>>

    @GET("api/admin/getEmployees")
    suspend fun getEmployees(): Response<DataResponse<Array<Employee>>>

    @GET("api/admin/getAppointments")
    suspend fun getAppointments(): Response<DataResponse<Array<Appointment>>>

    @POST("api/admin/addClient")
    suspend fun addClient(@Body client: Client): Response<DataResponse<String>>

    @POST("api/admin/addEmployee")
    suspend fun addEmployee(@Body employee: Employee): Response<DataResponse<String>>

    @POST("api/addAppointment")
    suspend fun addAppointment(@Body appointment: Appointment): Response<DataResponse<String>>

    @POST("api/admin/deleteClient")
    suspend fun deleteClient(@Query("id") id: String): Response<DataResponse<String>>

    @POST("api/admin/deleteEmployee")
    suspend fun deleteEmployee(@Query("id") id: String): Response<DataResponse<String>>

    @POST("api/admin/deleteAppointment")
    suspend fun deleteAppointment(@Query("id") id: String): Response<DataResponse<String>>

    @POST("api/admin/deleteBill")
    suspend fun deleteBill(@Query("id") id: String): Response<DataResponse<String>>
}