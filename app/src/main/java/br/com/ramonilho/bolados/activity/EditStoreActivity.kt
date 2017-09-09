package br.com.ramonilho.bolados.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.api.StoreAPI
import br.com.ramonilho.bolados.model.Store
import br.com.ramonilho.bolados.model.User
import br.com.ramonilho.bolados.utils.BToasty
import kotlinx.android.synthetic.main.fragment_edit_store.*
import retrofit2.Call
import retrofit2.Response
import android.widget.ArrayAdapter
import br.com.ramonilho.bolados.extensions.load
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import retrofit2.Callback


class EditStoreActivity : AppCompatActivity() {

    var storeAPI: StoreAPI? = APIUtils.storeAPIVersion
    var store = Store()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_edit_store)

        title = getString(R.string.edit_store_info)

        if (User.shared.storeId != 0) {
            storeAPI!!.store(User.shared.storeId).enqueue(object : retrofit2.Callback<Store> {
                override fun onResponse(call: Call<Store>?, response: Response<Store>?) {
                    if (response!!.isSuccessful) {
                        store = response.body()
                        updateFields()
                    } else {
                        Log.e("ProfileFragment", "Edit info failed.")
                        BToasty.toastErrorFrom(response.errorBody(), baseContext)
                    }
                }

                override fun onFailure(call: Call<Store>?, t: Throwable?) {
                    BToasty.show(getString(R.string.error_request), baseContext)
                }

            })
        }
    }

    fun updateFields() {
        etStoreName.setText(store.name)
        etStoreDescription.setText(store.description)

        val array_spinner = spCategory.adapter as ArrayAdapter<String>
        spCategory.setSelection(array_spinner.getPosition(store.category))

        etEmail.setText(store.email)
        etPhone.setText(store.phone)
        
        // Loading showcase images
        // It's not the prettiest but I'm gonna keep it
        if (store.pictures.size > 1) {
            ivShowcaseImage1.load(APIUtils.BASE_URL + store.pictures[0]) { p ->
                p.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fit().centerCrop()
                        .transform(RoundedCornersTransformation(10, 10))
            }
            ivShowcaseImage1.imageTintList = null
        }
        if (store.pictures.size > 2) {
            ivShowcaseImage2.load(APIUtils.BASE_URL + store.pictures[1]) { p ->
                p.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fit().centerCrop()
                        .transform(RoundedCornersTransformation(10, 10))
            }
            ivShowcaseImage2.imageTintList = null
        }
        if (store.pictures.size > 3) {
            ivShowcaseImage3.load(APIUtils.BASE_URL + store.pictures[2]) { p ->
                p.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fit().centerCrop()
                        .transform(RoundedCornersTransformation(10, 10))
            }
            ivShowcaseImage3.imageTintList = null
        }
        if (store.pictures.size > 4) {
            ivShowcaseImage4.load(APIUtils.BASE_URL + store.pictures[3]) { p ->
                p.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fit().centerCrop()
                        .transform(RoundedCornersTransformation(10, 10))
            }
            ivShowcaseImage4.imageTintList = null
        }
    }

    fun saveChanges(view: View) {
        Log.i("EditStore", "Saving changes...")

        storeAPI!!.editInfo(store).enqueue(object : retrofit2.Callback<Store> {
            override fun onFailure(call: Call<Store>?, t: Throwable?) {
                BToasty.show(getString(R.string.error_request), baseContext)
            }

            override fun onResponse(call: Call<Store>?, response: Response<Store>?) {
                if (response!!.isSuccessful) {
                    store = response.body()
                    updateFields()
                    BToasty.show(getString(R.string.changes_maded), baseContext)
                } else {
                    Log.e("ProfileFragment", "Edit info failed.")
                    BToasty.toastErrorFrom(response.errorBody(), baseContext)
                }
            }

        })
    }
}
