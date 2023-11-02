package com.full.crm.models

class Employee(
    id: String?,
    username: String?,
    password: String?,
    name: String?,
    surname: String?,
    email: String?,
    phone: String?,
    role: Role?,
    private var appointments: Array<Appointment>?,
    private var bills: Array<Bill>?,
    private var clients: Array<Client>?
) : User(id, username, password, name, surname, email, phone, role) {

    //region getters and setters
    public fun getAppointments(): Array<Appointment>? {
        return appointments
    }

    public fun setAppointments(appointments: Array<Appointment>?) {
        this.appointments = appointments
    }

    public fun getBills(): Array<Bill>? {
        return bills
    }

    public fun setBills(bills: Array<Bill>?) {
        this.bills = bills
    }

    public fun getClients(): Array<Client>? {
        return clients
    }

    public fun setClients(clients: Array<Client>?) {
        this.clients = clients
    }
    //endregion

    constructor() : this(null, null, null, null, null, null, null, null, null, null, null)

    @Override
    override fun toString(): String {
        var string = super.toString()

        string += "Appointments: \n"
        if (appointments != null) {
            for (appointment in appointments!!) {
                string += appointment.toString()
            }
        }

        string += "Bills: \n"
        if (bills != null) {
            for (bill in bills!!) {
                string += bill.toString()
            }
        }

        string += "Clients: \n"
        if (clients != null) {
            for (client in clients!!) {
                string += client.toString()
            }
        }

        return string
    }
}