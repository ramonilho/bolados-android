package br.com.ramonilho.bolados.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.api.UserAPI
import br.com.ramonilho.bolados.model.User
import br.com.ramonilho.bolados.utils.BToasty
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.callback.Callback

/**
 * Created by ramonhonorio on 10/09/17.
 */
class SignupActivity: AppCompatActivity() {

    val FLAG_SIGNUP = "SignupActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

    }

    fun signup(view: View){
        val userAPI = APIUtils.userAPIVersion

        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        val email = etEmail.text.toString()

        if (validate(username) && validate(password) && validate(email)) {

            val creationDate = SimpleDateFormat(getString(R.string.serverDateFormat)).format(Date())

            val newUser = User(0, email, password, username, creationDate, "Avenida Paulista 1100")

            userAPI.signup(newUser).enqueue(object : retrofit2.Callback<User> {
                override fun onResponse(call: Call<User>?, response: Response<User>?) {
                    if (response!!.isSuccessful) {

                        Log.i(FLAG_SIGNUP, "Signup successful.")

                        // Saving user into Singleton
                        User.shared = response.body()

                        // Instantiating a new activity
                        val intent = Intent(this@SignupActivity, MainActivity::class.java)
                        startActivity(intent)

                        BToasty.show(getString(R.string.login_successful), baseContext)

                    } else {
                        Log.e(FLAG_SIGNUP, "Signup failed.")
                        BToasty.toastErrorFrom(response.errorBody(), baseContext)
                    }
                }

                override fun onFailure(call: Call<User>?, t: Throwable?) {
                    BToasty.show(getString(R.string.error_request), baseContext)
                }

            })

        } else {
            BToasty.show(getString(R.string.invalid_input), baseContext)
        }

    }

    fun validate(field: String): Boolean {
        if (field.isEmpty() || field == "")
            return false
        if (field.count() < 3 )
            return false

        return true
    }

}