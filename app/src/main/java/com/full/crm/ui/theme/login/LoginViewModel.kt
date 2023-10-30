package com.full.crm.ui.theme.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.full.crm.models.Employee
import com.full.crm.navigation.AppNavigation
import com.full.crm.network.API
import com.full.crm.network.DataResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel: ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    //TODO: Navegacion con MVVM
    private var navigator : NavHostController? = null

    //TODO: Crear es isloading (para detectar cuando se ha cargado actualizarlo dentro del componible del login)
    //TODO: Crear el enableLogin (actualizarlo si el email contiene @ y el password es mayor de 7 caracteres)

    fun onLoginChanged(username: String, password: String) {
        _username.value = username
        _password.value = password
    }

    fun login() {
        viewModelScope.launch {
            var result = API.service.login(username.value!!, password.value!!)

            if (result.isSuccessful) {

                val data: DataResponse<Employee> = result.body()!!

                if (data.code == 200) {
                    //TODO: Navegar a la siguiente pantalla

                    Log.i("CRM", "Login correcto")
                    Log.i("CRM", data.toString())
                } else {
                    //Podria manejarse tambien los mensajes en el backend y mostrarlos aqui
                    when (data.code) {
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
    }
}