package br.com.ramonilho.bolados.model

import android.util.Log
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by ramonhonorio on 22/07/17.
 */

open class Favorite : RealmObject() {

    @PrimaryKey
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
    var pictures: RealmList<RealmString> = RealmList()
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    // Owner
    var userId: Int = 0

    fun storeObject() : Store {
        var store = Store()

        store.id = id
        store.name = name
        store.description = description
        store.category = category
        store.addressName = addressName
        store.city = city
        store.urlLogo = urlLogo
        store.email = email
        store.phone = phone
        store.rating = rating
        store.priceAverage = priceAverage
        store.latitude = latitude
        store.longitude = longitude

        var picList = ArrayList<String>()
        pictures.mapTo(picList) { it.value }
        store.pictures = picList

        return store
    }

    fun loadFrom(store: Store) {
        id               = store.id
        name             = store.name
        description      = store.description
        category         = store.category
        addressName      = store.addressName
        city             = store.city
        urlLogo          = store.urlLogo
        email            = store.email
        phone            = store.phone
        rating           = store.rating
        priceAverage     = store.priceAverage
        latitude         = store.latitude
        longitude        = store.longitude

        store.pictures.map {
            val str = RealmString()
            str.value = it

            Log.i("Favorite", "picture: "+str.value)

            pictures.add(str)
        }
    }

}