package br.com.ramonilho.bolados.activity

import android.util.Log
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.model.MockUser
import br.com.ramonilho.bolados.utils.BToasty
import io.realm.Realm
import io.realm.RealmConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : android.support.v7.app.AppCompatActivity() {
    //Tempo que o splashscreen ficará visivel (em milissegundos)
    private val SPLASH_DISPLAY_LENGTH = 1000 //3500
    private val SPLASH_FLAG = "SplashActivity"

    var realm: Realm? = null

    public override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // To ignore migration
//        try {
//            Realm.removeDefaultConfiguration()
//            Realm.init(applicationContext)
////
//            val config = RealmConfiguration.Builder()
//                    .deleteRealmIfMigrationNeeded()
//                    .build()
//            Realm.setDefaultConfiguration(config)
//        } catch (exception: Exception) {
//            Log.i(SPLASH_FLAG, "Realm already initialized")
//        }

        // To init Realm
        Realm.init(applicationContext)
        realm = Realm.getDefaultInstance()

//        realm!!.beginTransaction()
//        realm!!.deleteAll()
//        realm!!.commitTransaction()

        // Executando o método que iniciará nossa animação
        loadAnimation()
        getMockedUser()
    }

    private fun loadAnimation() {
        val anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.animation_splash)
        anim.reset()
        //Pegando o nosso objeto criado no layout
        val iv = findViewById(R.id.splash) as android.widget.ImageView

        // Caso a imagem exista, executar a animacao
        if (iv != null) {
            iv.clearAnimation()
            iv.startAnimation(anim)
        }

        android.os.Handler().postDelayed({
            // After delay, it goes to next Activity
            val intent = android.content.Intent(this@SplashActivity, LoginActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)

            // Finishes default activity
            this@SplashActivity.finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    private fun getMockedUser() {
        val mockAPI = APIUtils.mockAPIVersion

        mockAPI.checkMocked().enqueue(object : Callback<MockUser> {
            override fun onFailure(call: Call<MockUser>?, t: Throwable?) {
                Log.e(SPLASH_FLAG, "Error while getting mockedUser: " + t!!.localizedMessage)
            }

            override fun onResponse(call: Call<MockUser>?, response: Response<MockUser>?) {
                if (response!!.isSuccessful) {
                    MockUser.shared = response.body()

                    // Saving mockedUser in database
                    realm!!.beginTransaction()
                    if (realm!!.where(MockUser::class.java).findAll().isEmpty()) {
                        realm!!.copyToRealm(MockUser.shared)
                    }
                    realm!!.commitTransaction()

                }
            }

        })
    }
}