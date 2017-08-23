package br.com.ramonilho.bolados.model

/**
 * Created by ramonhonorio on 19/08/17.
 */

class User {
    constructor()

    companion object {
        var shared: User = User()
    }

    var id: Int? = 0

    // Login attributes
    var email: String? = null
    var password: String? = null

    // Informations
    var name: String? = null
    var photo: String? = null

    // Ownership
    var storeId: Int? = 0
}