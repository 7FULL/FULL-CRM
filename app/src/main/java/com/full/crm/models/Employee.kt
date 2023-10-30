package com.full.crm.models
class Employee {
    private var username: String? = null
    private var password: String? = null
    private var name: String? = null
    private var surname: String? = null
    private var email: String? = null
    private var phone: String? = null
    private var appointments: Array<Appointment>? = null

    fun Employee() {}

    fun Employee(
        username: String?,
        password: String?,
        name: String?,
        surname: String?,
        email: String?,
        phone: String?,
        appointments: Array<Appointment>?
    ) {
        this.username = username
        this.password = password
        this.name = name
        this.surname = surname
        this.email = email
        this.phone = phone
        this.appointments = appointments
    }

    //region getters and setters

    fun getUsername(): String? {
        return username
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getSurname(): String? {
        return surname
    }

    fun setSurname(surname: String?) {
        this.surname = surname
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getPhone(): String? {
        return phone
    }

    fun setPhone(phone: String?) {
        this.phone = phone
    }
    //endregion

    override fun toString(): String {
        var x = "Employee(username=$username, password=$password, name=$name, surname=$surname, email=$email, phone=$phone"

        if (appointments != null) {
            x += ", appointments=["
            for (appointment in appointments!!) {
                x += "$appointment, "
            }
            x += "]"
        }

        x += ")"

        return x
    }
}