package br.com.ramonilho.bolados.application

import android.app.Application
import com.facebook.stetho.Stetho
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by ramonhonorio on 22/07/17.
 */

class BoladosApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

    }
}