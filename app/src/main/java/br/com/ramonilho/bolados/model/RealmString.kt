package br.com.ramonilho.bolados.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by ramonhonorio on 14/09/17.
 */
open class RealmString: RealmObject() {

    @PrimaryKey
    var value: String = ""
}