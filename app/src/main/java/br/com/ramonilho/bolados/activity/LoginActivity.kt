package br.com.ramonilho.bolados.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.model.MockUser
import br.com.ramonilho.bolados.model.Store
import br.com.ramonilho.bolados.model.User
import br.com.ramonilho.bolados.utils.BToasty
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class LoginActivity : AppCompatActivity() {
    
    private val FLAG_LOGIN_ACTIVITY = "LoginActivity"
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        realm = Realm.getDefaultInstance()

        // If debugging
//        etUsername.setText("ramon@email.com")
//        etPassword.setText("1234")
        etUsername.setText("android")
        etPassword.setText("mobile")

        val realmQuery = realm!!.where(MockUser::class.java)
        val realmResults = realmQuery.findAll()

        realmResults.map { result ->
            Log.i(FLAG_LOGIN_ACTIVITY, "realm result: "+result.usuario+" || "+result.id)
        }

    }

    fun onLogin(view: View) {
        if (this.checkOfflineLogin()) {
            requestMockedUser()
            return
        }

        val userAPI = APIUtils.userAPIVersion

        // Getting text from editTexts
        val login = etUsername.text.toString()
        val password = etPassword.text.toString()

        userAPI.authenticate(login, password).enqueue(object : retrofit2.Callback<User> {
            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                if (response!!.isSuccessful) {
                    Log.i(FLAG_LOGIN_ACTIVITY, "Authentication successful.")

                    // Saving user into Singleton
                    User.shared = response.body()

                    // Instantiating a new activity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)

                    BToasty.show(getString(R.string.login_successful), baseContext)

                } else {
                    Log.e(FLAG_LOGIN_ACTIVITY, "Authentication failed.")
                    BToasty.toastErrorFrom(response.errorBody(), baseContext)
                }
            }

            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.e(FLAG_LOGIN_ACTIVITY, "Error while getting User: " + t!!.localizedMessage)
            }
        })
    }

    fun onSignup(view: View)  {
        val storeAPI = APIUtils.storeAPIVersion

        storeAPI.store(1).enqueue(object : Callback<Store> {
            override fun onResponse(call: Call<Store>?, response: Response<Store>?) {
                if (response!!.isSuccessful) {
                    Log.i(FLAG_LOGIN_ACTIVITY, "Getted Store with success: "+response.body().name)
                } else {
                    Log.e(FLAG_LOGIN_ACTIVITY, "Authentication failed.")
                    BToasty.toastErrorFrom(response.errorBody(), baseContext)
                }
            }

            override fun onFailure(call: Call<Store>?, t: Throwable?) {
                Log.e(FLAG_LOGIN_ACTIVITY, "Error while signing up user: " + t!!.localizedMessage)
            }

        })
    }


    // MOCK checks:
    fun checkOfflineLogin () : Boolean {
        val realmResults = realm!!.where(MockUser::class.java).findAll()

        val login = etUsername.text.toString()
        val password = etPassword.text.toString()

        for (result in realmResults)
            if (login == result.usuario && password == result.senha)
                return true

        return false
    }

    fun requestMockedUser () {
        val userAPI = APIUtils.userAPIVersion

        Log.i(FLAG_LOGIN_ACTIVITY, "RequestURL: "+userAPI.mockedUser().request().url().toString())

        userAPI.mockedUser().enqueue(object : Callback<User>{
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.e(FLAG_LOGIN_ACTIVITY, "Error while getting mocked user: " + t!!.localizedMessage)
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                if (response!!.isSuccessful) {
                    Log.i(FLAG_LOGIN_ACTIVITY, "Authentication successful.")

                    // Saving user into Singleton
                    User.shared = response.body()

                    // Instantiating a new activity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)

                    BToasty.show(getString(R.string.login_successful), baseContext)

                } else {
                    Log.e(FLAG_LOGIN_ACTIVITY, "Authentication failed")
                    BToasty.toastErrorFrom(response.errorBody(), baseContext)
                }
            }

        })
    }

}
