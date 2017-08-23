package br.com.ramonilho.bolados.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.model.Store
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.activity_home_detail.*

class HomeDetailActivity : AppCompatActivity() {

    var store = Store()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_detail)

        // Getting store from Extras
        val jsonStore = intent.extras.getString("store")
        store = Gson().fromJson(jsonStore, Store::class.java)

        // Set ActionBar Title text
//        setTitle(store.name)
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

        btMap.text = store.addressName+"\n"+store.city
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
        Picasso.with(applicationContext)
                .load(APIUtils.BASE_URL + store.pictures!![0])
                .placeholder(R.mipmap.ic_launcher)
                .fit().centerCrop()
                .transform(RoundedCornersTransformation(10, 10))
                .error(R.mipmap.ic_launcher)
                .into(ivShowcaseImage1)

        Picasso.with(applicationContext)
                .load(APIUtils.BASE_URL + store.pictures!![1])
                .placeholder(R.mipmap.ic_launcher)
                .fit().centerCrop()
                .transform(RoundedCornersTransformation(10, 10))
                .error(R.mipmap.ic_launcher)
                .into(ivShowcaseImage2)

        if (store.pictures!!.count() > 2) {
            Picasso.with(applicationContext)
                    .load(APIUtils.BASE_URL + store.pictures!![2])
                    .placeholder(R.mipmap.ic_launcher)
                    .fit().centerCrop()
                    .transform(RoundedCornersTransformation(10, 10))
                    .error(R.mipmap.ic_launcher)
                    .into(ivShowcaseImage3)
        }

        if (store.pictures!!.count() > 3) {
            Picasso.with(applicationContext)
                    .load(APIUtils.BASE_URL + store.pictures!![3])
                    .placeholder(R.mipmap.ic_launcher)
                    .fit().centerCrop()
                    .transform(RoundedCornersTransformation(10, 10))
                    .error(R.mipmap.ic_launcher)
                    .into(ivShowcaseImage4)
        }

    }

}
