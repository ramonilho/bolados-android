package br.com.ramonilho.bolados.activity

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.model.Store
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_home_detail.*
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Button
import br.com.ramonilho.bolados.fragment.FavoritesFragment
import br.com.ramonilho.bolados.model.Favorite
import br.com.ramonilho.bolados.utils.BToasty
import io.realm.Realm


class HomeDetailActivity : AppCompatActivity() {

    var store = Store()
    var isFavorite: Boolean = false

    var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_detail)

        // Initialize realm
        initRealm()

        // Getting store from Extras
        val jsonStore = intent.extras.getString("store")
        store = Gson().fromJson(jsonStore, Store::class.java)
        isFavorite = intent.extras.getBoolean("isFavorite", false)
        favorited()

        // Set ActionBar Title text
        title = getString(R.string.details)

        // Loading cell informations
        loadInfo()
    }
    
    fun loadInfo() {
        tvName.text = store.name
        tvCategory.text = store.category

        val priceAverage = store.priceAverage!!
        tvPriceAverage.text = "• " + ("$".repeat(priceAverage))
        tvRating.text = store.rating.toString()
        tvDescription.text = store.description.toString()

        if(store.city != null) {
            btMap.text = store.addressName+"\n"+store.city
        } else {
            btMap.text = store.addressName
        }

        btMap.text = store.addressName

//        btEmail.text = store.email
        btPhone.text = store.phone

        // Store Logo
        Picasso.with(applicationContext)
                .load(APIUtils.BASE_URL + store.urlLogo)
                .fit()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(ivLogo)

        // Store ShowImage
        if (store.pictures!!.size > 1) {
            Picasso.with(applicationContext)
                    .load(APIUtils.BASE_URL + store.pictures!![0])
                    .placeholder(R.mipmap.ic_launcher)
                    .fit().centerCrop()
                    .transform(RoundedCornersTransformation(10, 10))
                    .error(R.mipmap.ic_launcher)
                    .into(ivShowcaseImage1)
        }

        if (store.pictures!!.size > 1) {
            Picasso.with(applicationContext)
                    .load(APIUtils.BASE_URL + store.pictures!![1])
                    .placeholder(R.mipmap.ic_launcher)
                    .fit().centerCrop()
                    .transform(RoundedCornersTransformation(10, 10))
                    .error(R.mipmap.ic_launcher)
                    .into(ivShowcaseImage2)
        }

        if (store.pictures!!.size > 2) {
            Picasso.with(applicationContext)
                    .load(APIUtils.BASE_URL + store.pictures!![2])
                    .placeholder(R.mipmap.ic_launcher)
                    .fit().centerCrop()
                    .transform(RoundedCornersTransformation(10, 10))
                    .error(R.mipmap.ic_launcher)
                    .into(ivShowcaseImage3)
        }

        if (store.pictures!!.size > 3) {
            Picasso.with(applicationContext)
                    .load(APIUtils.BASE_URL + store.pictures!![3])
                    .placeholder(R.mipmap.ic_launcher)
                    .fit().centerCrop()
                    .transform(RoundedCornersTransformation(10, 10))
                    .error(R.mipmap.ic_launcher)
                    .into(ivShowcaseImage4)
        }

        btFavorite.setOnClickListener {
            realm = Realm.getDefaultInstance()

            // Saving store in database

            if (favorited()) {
                realm!!.executeTransaction { realm ->
                    val results = realm.where(Favorite::class.java).equalTo("id", store.id).findAll()

                    if (results.isNotEmpty()) {
                        val realmObj = results.first()
                        realmObj.deleteFromRealm()
                    }
                    isFavorite = false
                }
            }
            else {
                realm!!.executeTransaction { realm ->
                    val fav = Favorite()
                    fav.loadFrom(store)
                    realm.copyToRealmOrUpdate(fav)
                    isFavorite = true
                }
            }

            updateFavoriteButton()
        }

        updateFavoriteButton()

    }

    fun phoneCall(view: View) {

        val phoneNumber = store.phone!!.replace("(", "").replace(")", "").replace(" ", "").replace("-", "")
        val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null))
        startActivity(intent)

    }

    fun sendEmail(view: View) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(store.email!!))
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject)+" "+store.name!!)
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body))
        try {
            startActivity(Intent.createChooser(i, "Send mail..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            BToasty.show(getString(R.string.no_email_clients_installed), baseContext)
        }
    }

    fun showMap(view: View) {
        val intent = Intent(this@HomeDetailActivity, MapsActivity::class.java)
        intent.putExtra("latitude", store.latitude)
        intent.putExtra("longitude", store.longitude)
        intent.putExtra("storeName", store.name)
        startActivity(intent)
    }

    fun favorited(): Boolean {
        if (isFavorite == false)
            if (realm!!.where(Favorite::class.java).equalTo("id", store.id).findAll().isEmpty())
                return false

        return true
    }

    fun updateFavoriteButton() {

        if(favorited()) {
            btFavorite.text = getString(R.string.remove_favorites)

            Log.i("btFavorite.text", btFavorite.text.toString())

            (findViewById(R.id.btFavorite) as Button).text = getString(R.string.remove_favorites)
        } else {
            btFavorite.text = getString(R.string.save_favorites)

            Log.i("btFavorite.text", btFavorite.text.toString())

            (findViewById(R.id.btFavorite) as Button).text = getString(R.string.save_favorites)
        }
    }

    fun initRealm() {
        realm = Realm.getDefaultInstance()
    }

    override fun onBackPressed() {
        setResult(321)
        finish()
        super.onBackPressed()
    }

}
