package br.com.ramonilho.bolados.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.activity.HomeDetailActivity
import br.com.ramonilho.bolados.adapter.StoreAdapter
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.api.StoreAPI
import br.com.ramonilho.bolados.listener.OnItemClickListener
import br.com.ramonilho.bolados.model.Favorite
import br.com.ramonilho.bolados.model.Store
import br.com.ramonilho.bolados.utils.BToasty
import com.google.gson.Gson
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.fragment_favorites.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FavoritesFragment : Fragment(), View.OnClickListener{

    var storeAdapter: StoreAdapter? = null
    var storeAPI: StoreAPI? = null

    var realm: Realm? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater!!.inflate(R.layout.fragment_favorites, container, false)

        initRealm()

        storeAdapter = StoreAdapter(ArrayList<Store>(), listener = object : OnItemClickListener {
            override fun onItemClick(item: Store) {

                val myIntent = Intent(activity, HomeDetailActivity::class.java)

                val jsonStore = Gson().toJson(item)
                myIntent.putExtra("store", jsonStore)
                myIntent.putExtra("isFavorite", true)

                activity.startActivityForResult(myIntent, 321)
            }
        })

        val layoutManager = LinearLayoutManager(this.activity)
        itemView.rvStores.layoutManager = layoutManager
        itemView.rvStores.adapter = storeAdapter
        itemView.rvStores.setHasFixedSize(true)

        carregaDados()

        return itemView
    }

    override fun onClick(v: View) {
        Log.i("FavoritesFragment", "OnClick called")
    }

    private fun carregaDados() {

        Log.i("FavoritesFrag", "realm >> "+realm)

        val realmResults = realm!!.where(Favorite::class.java).findAll()

        realmResults.map {
            Log.i("FavoritesFrag", "<<>> "+it.name)
        }

        if (realmResults != null) {
            val favorites = realm!!.copyFromRealm(realmResults)

            val stores = favorites.map { it.storeObject() }

            storeAdapter!!.update(stores)

            if (stores.isEmpty()) {
                BToasty.show("No favorites where found", context)
            }

        } else {
            BToasty.show("No favorites where found", context)
        }

    }

    fun initRealm() {
        realm = Realm.getDefaultInstance()
    }

}