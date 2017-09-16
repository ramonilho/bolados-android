package br.com.ramonilho.bolados.model

import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by ramonhonorio on 22/07/17.
 */

class Store {

    var id: Int = 0
    var name: String = ""
    var description: String = ""
    var category: String = ""
    var addressName: String = ""
    var city: String = ""
    var urlLogo: String = ""
    var email: String = ""
    var phone: String = ""
    var rating: Double = 0.0
    var priceAverage: Int = 0
    var pictures: List<String> = ArrayList()
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    // Owner
    var userId: Int? = 0

    companion object {
        var fromUser: Store = Store()
    }

    fun favoriteObject() : Favorite {
        var favorite = Favorite()

        favorite.id = id
        favorite.name = name
        favorite.description = description
        favorite.category = category
        favorite.addressName = addressName
        favorite.city = city
        favorite.urlLogo = urlLogo
        favorite.email = email
        favorite.phone = phone
        favorite.rating = rating
        favorite.priceAverage = priceAverage
        favorite.latitude = latitude
        favorite.longitude = longitude

//        var picList = RealmList<RealmString>()
//        pictures.mapTo(picList) {
//            val str = RealmString()
//            str.value = it
//            str
//        }

//        favorite.pictures = picList

        return favorite
    }
}

/*
// JSON Model:
    {
        "id" : 101,
        "name" : "Tradicional",
        "category" : "Cakes",
        "description" : "O segredo da Tradicional Bolos Caseiros é a fabricação artesanal, fazendo com que cada pedaço de bolo seja legitimamente caseiro.",
        "addressName" : "Rua Clodomiro Amazonas, 1200\nSão Paulo - SP, Brasil",
        "city" : "São Paulo - SP",
        "urlLogo" : "/cake-images/tradicional-3.png",
        "email" : "tradicional@email.com",
        "phone" : "(11) 4002-8922",
        "rating" : 4.7,
        "priceAverage" : 3,
        "pictures" : [
            "/cake-images/tradicional-3.png", "/cake-images/tradicional-1.jpg", "/cake-images/tradicional-2.jpg", "/cake-images/tradicional-4.jpg"
        ],
        "latitude": -23.5644,
        "longitude": -46.6526
    }
*/