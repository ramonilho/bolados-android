package br.com.ramonilho.bolados.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.adapter.StoreAdapter
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.api.StoreAPI
import br.com.ramonilho.bolados.listener.OnItemClickListener
import br.com.ramonilho.bolados.model.Store
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import android.content.Intent
import br.com.ramonilho.bolados.activity.HomeDetailActivity
import com.google.gson.Gson


/**
 * Created by ramonhonorio on 22/07/17.
 */


class HomeFragment : Fragment(), View.OnClickListener {

    var storeAdapter: StoreAdapter? = null
    var storeAPI: StoreAPI? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater!!.inflate(R.layout.fragment_home, container, false)

        storeAdapter = StoreAdapter(ArrayList<Store>(), listener = object : OnItemClickListener {
            override fun onItemClick(item: Store) {

                val myIntent = Intent(activity, HomeDetailActivity::class.java)

                val jsonStore = Gson().toJson(item)
                myIntent.putExtra("store", jsonStore)

                activity.startActivity(myIntent)

//                Toast.makeText(inflater.context, item.name, Toast.LENGTH_SHORT).show()
            }
        })

//        itemView.setOnClickListener(this)
        val layoutManager = LinearLayoutManager(this.activity)
        itemView.rvStores.layoutManager = layoutManager
        itemView.rvStores.adapter = storeAdapter
        itemView.rvStores.setHasFixedSize(true)

        carregaDados()

        return itemView
    }

    override fun onClick(v: View) {
        Log.i("HomeFragment", "OnClick called")
    }

    private fun carregaDados() {
        storeAPI = APIUtils.storeAPIVersion

        storeAPI!!.stores.enqueue(object : Callback<List<Store>> {
            override fun onResponse(call: Call<List<Store>>?, response: Response<List<Store>>?) {
                if (response!!.isSuccessful) {
                    Log.i("HomeFragStore", "Dados carregados com sucesso")
                    storeAdapter!!.update(response!!.body())
                }
            }

            override fun onFailure(call: Call<List<Store>>?, t: Throwable?) {
                Log.e("HomeFragStore", "Erro ao carregar dados: " + t!!.localizedMessage)
            }
        })
    }
}