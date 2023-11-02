package com.full.crm.models

open class User(
    private var id:String?,
    private var username: String?,
    private var password: String?,
    private var name: String?,
    private var surname: String?,
    private var email: String?,
    private var phone: String?,
    private var role: Role?
) {

    //region getters and setters
    public fun getUsername(): String? {
        return username
    }

    public fun setUsername(username: String?) {
        this.username = username
    }

    public fun getPassword(): String? {
        return password
    }

    public fun setPassword(password: String?) {
        this.password = password
    }

    public fun getName(): String? {
        return name
    }

    public fun setName(name: String?) {
        this.name = name
    }

    public fun getSurname(): String? {
        return surname
    }

    public fun setSurname(surname: String?) {
        this.surname = surname
    }

    public fun getEmail(): String? {
        return email
    }

    public fun setEmail(email: String?) {
        this.email = email
    }

    public fun getPhone(): String? {
        return phone
    }

    public fun setPhone(phone: String?) {
        this.phone = phone
    }

    public fun getRole(): Role? {
        return role
    }

    public fun setRole(role: Role?) {
        this.role = role
    }

    //endregion

    //TOString
    @Override
    override fun toString(): String {
        return "ID: " + id + "\n" +
                "Username: " + username + "\n" +
                "Password: " + password + "\n" +
                "Name: " + name + "\n" +
                "Surname: " + surname + "\n" +
                "Email: " + email + "\n" +
                "Phone: " + phone + "\n" +
                "Role: " + role + "\n"
    }
}