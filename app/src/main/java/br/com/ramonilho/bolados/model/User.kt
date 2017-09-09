package br.com.ramonilho.bolados.model

/**
 * Created by ramonhonorio on 19/08/17.
 */

class User {
    constructor()
    constructor(id: Int, email: String, password: String, name: String, creationDate: String, streetAddress: String) {
        this.id = id
        this.email = email
        this.name = name
        this.password = password
        this.creationDate = creationDate
        this.streetAddress = streetAddress
    }

    companion object {
        var shared: User = User()
    }

    var id: Int = 0

    // Login attributes
    var email: String = ""
    var password: String = ""

    // Informations
    var name: String = ""
    var photo: String = ""
    var creationDate: String = ""
    var streetAddress: String = ""

    // Ownership
    var storeId: Int = 0

    fun clone () : User {
        return User(id, email, password, name, creationDate, streetAddress)
    }



}