package br.com.ramonilho.bolados.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.model.MockUser
import br.com.ramonilho.bolados.model.User
import br.com.ramonilho.bolados.utils.BToasty
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.facebook.AccessToken





class LoginActivity : AppCompatActivity() {
    
    private val FLAG_LOGIN_ACTIVITY = "LoginActivity"
    private var realm: Realm? = null
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        realm = Realm.getDefaultInstance()

        // If debugging
        etUsername.setText("android")
        etPassword.setText("mobile")

        if (isLoggedInFacebook()) {
            requestMockedUser()
        } else {
            callbackManager = CallbackManager.Factory.create()
            btFbLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    BToasty.show(getString(R.string.login_successful), baseContext)
                    requestMockedUser()
                }

                override fun onCancel() {
                    BToasty.show(getString(R.string.auth_failed), baseContext)
                }

                override fun onError(error: FacebookException?) {
                    BToasty.show(getString(R.string.auth_failed), baseContext)
                }

            })
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
        Log.i(FLAG_LOGIN_ACTIVITY, "Hereeeee")
        val intent = Intent(this@LoginActivity, SignupActivity::class.java)
        startActivity(intent)
//        val intent = Intent(this@LoginActivity, SignupActivity::class.java)
//        startActivity(intent)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    fun isLoggedInFacebook(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null
    }

}
