package br.com.ramonilho.bolados.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by ramonhonorio on 09/09/17.
 */
open class MockUser: RealmObject() {

    @PrimaryKey
    var id: Long = 0

    var usuario : String = ""
    var senha : String = ""

    companion object {
        var shared: MockUser = MockUser()
    }
}