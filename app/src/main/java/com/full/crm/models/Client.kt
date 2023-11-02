package com.full.crm.models

class Client(
    id: String?,
    username: String?,
    password: String?,
    name: String?,
    surname: String?,
    email: String?,
    phone: String?,
    role: Role?,
    private var appointments: Array<Appointment>?,
    private var bills: Array<Bill>?
) : User(id, username, password, name, surname, email, phone, role) {
    constructor() : this(null, null, null, null, null, null, null, null, null, null)
}