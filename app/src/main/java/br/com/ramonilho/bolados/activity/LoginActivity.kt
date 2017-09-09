package br.com.ramonilho.bolados.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.model.Store
import br.com.ramonilho.bolados.model.User
import br.com.ramonilho.bolados.utils.BToasty
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername.setText("ramon@email.com")
        etPassword.setText("1234")

    }

    fun onLogin(view: View) {

        val userAPI = APIUtils.userAPIVersion

        // Getting text from editTexts
        val login = etUsername.text.toString()
        val password = etPassword.text.toString()

        userAPI.authenticate(login, password).enqueue(object : retrofit2.Callback<User> {
            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                if (response!!.isSuccessful) {
                    Log.i("LoginFrag", "Authentication successful.")

                    // Saving user into Singleton
                    User.shared = response.body()

                    // Instantiating a new activity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)

                    BToasty.show(getString(R.string.login_successful), baseContext)

                } else {
                    Log.e("LoginFrag", "Authentication failed.")
                    BToasty.toastErrorFrom(response.errorBody(), baseContext)
                }
            }

            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.e("LoginFrag", "Erro ao carregar dados: " + t!!.localizedMessage)
            }
        })
    }

    fun onSignup(view: View)  {
        val storeAPI = APIUtils.storeAPIVersion

        storeAPI.store(1).enqueue(object : Callback<Store> {
            override fun onResponse(call: Call<Store>?, response: Response<Store>?) {
                if (response!!.isSuccessful) {
                    Log.i("LoginFrag", "Store get with success: "+response.body().name)
                } else {
                    Log.e("LoginFrag", "Authentication failed.")
                    BToasty.toastErrorFrom(response.errorBody(), baseContext)
                }
            }

            override fun onFailure(call: Call<Store>?, t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

}
