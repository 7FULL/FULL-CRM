package com.full.crm.login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    //TODO: Crear es isloading (para detectar cuando se ha cargado actualizarlo dentro del componible del login)
    //TODO: Crear el enableLogin (actualizarlo si el email contiene @ y el password es mayor de 7 caracteres)

    fun onLoginChanged(username: String, password: String) {
        _username.value = username
        _password.value = password
    }
}