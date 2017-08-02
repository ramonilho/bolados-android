package br.com.ramonilho.bolados.model

/**
 * Created by ramonhonorio on 22/07/17.
 */

class Store {
    var id: Int? = 0
    var name: String? = null
    var description: String? = null
    var category: String? = null
    var addressName: String? = null
    var city: String? = null
    var urlLogo: String? = null
    var email: String? = null
    var phone: String? = null
    var rating: Double? = 0.0
    var priceAverage: Int? = 0
    var pictures: List<String>? = ArrayList()

    constructor()
}

/*
// JSON Model:
    {
        "id" : 1,
        "name" : "Tradicional",
        "category" : "Cakes",
        "addressName" : "Rua Clodomiro Amazonas, 1200",
        "urlLogo" : "/cake-images/tradicional-3.png",
        "rating" : 4.7,
        "priceAverage" : 3,
        "pictures" : [
        "/cake-images/tradicional-1.png", "/cake-images/tradicional-2.png", "/cake-images/tradicional-4.png"
        ]
    }
*/