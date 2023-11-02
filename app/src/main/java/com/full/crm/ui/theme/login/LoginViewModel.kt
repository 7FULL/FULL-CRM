package com.full.crm.ui.theme.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.full.crm.models.Employee
import com.full.crm.navigation.NavigationManager
import com.full.crm.network.API
import com.full.crm.network.DataResponse
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val auth = Firebase.auth

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    //TODO: Crear es isloading (para detectar cuando se ha cargado actualizarlo dentro del componible del login)
    //TODO: Crear el enableLogin (actualizarlo si el email contiene @ y el password es mayor de 7 caracteres)

    fun signInWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            try{
                auth.signInWithCredential(credential).addOnCompleteListener {
                    task-> if(task.isSuccessful) {
                        Log.i("CRM", "Login correcto con google")

                        val email = task.result.user?.email

                    if (email != null) {
                        Log.i("CRM", email)

                        viewModelScope.launch {
                            var result = API.service.login(email!!, "")

                            if (result.isSuccessful) {
                                val data: DataResponse<Employee> = result.body()!!

                                if (data.code == 200) {
                                    NavigationManager.instance?.navigate("agenda")
                                }
                                else if (data.code == 401) {
                                     _error.value = "Usuario no reconocido"
                                }
                            } else {
                                Log.i("CRM", "Error en el login con google")
                                Log.i("CRM", result.toString())
                            }
                        }
                    } else{
                        Log.i("CRM", "Error en el login con google")
                        Log.i("CRM", "El email es nulo")
                    }
                    } else {
                        Log.i("CRM", "Error en el login con google")
                        Log.i("CRM", task.exception.toString())
                    }
                }.addOnFailureListener {
                    Log.i("CRM", "Error en el login con google")
                    Log.i("CRM", it.toString())
                }
            }
            catch (e: Exception) {
                Log.i("CRM", "Error en el login con google")
                Log.i("CRM", e.toString())
            }
        }
    }

    fun onLoginChanged(username: String, password: String) {
        _username.value = username
        if (password.length < 8) {
            _password.value = password
        }
    }

    fun login() {
        viewModelScope.launch {
            try{
                var result = API.service.login(username.value!!, password.value!!)

                if (result.isSuccessful) {

                    val response: DataResponse<Employee> = result.body()!!

                    if (response.code == 200) {
                        API.setUser(response.data!!)

                        Log.i("CRM", "Login correcto")
                        Log.i("CRM", response.data.toString())

                        NavigationManager.instance?.navigate("agenda")
                    } else {
                        //Podria manejarse tambien los mensajes en el backend y mostrarlos aqui
                        when (response.code) {
                            401 -> {
                                _error.value = "Usuario o contraseña incorrectos"
                            }
                            500 -> {
                                _error.value = "Error en el servidor"
                            }
                            else -> {
                                _error.value = "Error desconocido"
                            }
                        }

                        //Limpiamos el password y email para usabilidad
                        _password.value = ""
                        _username.value = ""
                    }

                } else {
                    Log.i("CRM", "Error en el login")
                    Log.i("CRM", result.toString())
                }
            }
            catch (e: Exception) {
                Log.i("CRM", "Error en el login")
                Log.i("CRM", e.toString())
                _error.value = "Lo sentimos nuestros servidores no estan disponibles en estos momentos"
            }
        }
    }
}