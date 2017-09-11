package br.com.ramonilho.bolados.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.activity.EditStoreActivity
import br.com.ramonilho.bolados.api.StoreAPI
import kotlinx.android.synthetic.main.fragment_store.view.*
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.model.Store
import br.com.ramonilho.bolados.model.User
import br.com.ramonilho.bolados.utils.BToasty
import br.com.ramonilho.bolados.extensions.load
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import retrofit2.Call
import retrofit2.Response


class StoreFragment : Fragment(), View.OnClickListener {

    var storeAPI: StoreAPI? = APIUtils.storeAPIVersion
    var store = Store()
    lateinit var itemView: View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater!!.inflate(R.layout.fragment_store, container, false)

        if (User.shared.storeId != 0) {
            storeAPI!!.store(User.shared.storeId).enqueue(object : retrofit2.Callback<Store> {
                override fun onResponse(call: Call<Store>?, response: Response<Store>?) {
                    if (response!!.isSuccessful) {
                        store = response.body()
                        updateFields()
                    } else {
                        Log.e("ProfileFragment", "Edit info failed.")
                        BToasty.toastErrorFrom(response.errorBody(), context)

                        itemView.alpha = 0F
                    }
                }

                override fun onFailure(call: Call<Store>?, t: Throwable?) {
                    BToasty.show(getString(R.string.error_request), context)
                    itemView.alpha = 0F
                }

            })
        }

        // Edit button action
        itemView.ibEditStore.setOnClickListener(View.OnClickListener {

            val intent = Intent(activity, EditStoreActivity::class.java)
            startActivity(intent)

        })

        this.itemView = itemView
        return itemView
    }

    fun updateFields() {
        itemView.tvName.text = store.name
        itemView.tvDescription.text = store.description
        itemView.tvCategory.text = store.category
        itemView.tvEmail.text = store.email
        itemView.tvPhone.text = store.phone
        itemView.tvAddress.text = store.addressName

        // Loading Image with Picasso and Kotlin extension (ExtImageView.kt)
        itemView.ivLogo.load(APIUtils.BASE_URL + store.urlLogo) { p ->
            p.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
        }

        // Loading showcase images
        // It's not the prettiest but I'm gonna keep it
        if (store.pictures.size > 1) {
            itemView.ivShowcaseImage1.load(APIUtils.BASE_URL + store.pictures[0]) { p ->
                p.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fit().centerCrop()
                        .transform(RoundedCornersTransformation(10, 10))
            }
        }
        if (store.pictures.size > 2) {
            itemView.ivShowcaseImage2.load(APIUtils.BASE_URL + store.pictures[1]) { p ->
                p.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fit().centerCrop()
                        .transform(RoundedCornersTransformation(10, 10))
            }
        }
        if (store.pictures.size > 3) {
            itemView.ivShowcaseImage3.load(APIUtils.BASE_URL + store.pictures[2]) { p ->
                p.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fit().centerCrop()
                        .transform(RoundedCornersTransformation(10, 10))
            }
        }
        if (store.pictures.size > 4) {
            itemView.ivShowcaseImage4.load(APIUtils.BASE_URL + store.pictures[3]) { p ->
                p.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fit().centerCrop()
                        .transform(RoundedCornersTransformation(10, 10))
            }
        }
    }

    override fun onClick(v: View) {}

}