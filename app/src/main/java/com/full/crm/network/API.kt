package com.full.crm.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//https://developer.android.com/studio/run/emulator-networking
//Tenemos que poner 10.0.2.2 en vez de localhost porque estamos en un emulador
private val BASE_URL = "http://10.0.2.2:8080/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

// Lo hacemos singleton porque en uno de los codlabs de Google lo recomiendan porque el create es costoso
// Singleton (usamos lazy para que se cree cuando se necesite y no antes)
object API {
    val service : ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}