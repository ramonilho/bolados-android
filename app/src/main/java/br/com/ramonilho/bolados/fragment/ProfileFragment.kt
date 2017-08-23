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
import br.com.ramonilho.bolados.model.Store
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class ProfileFragment : Fragment(), View.OnClickListener {

    var storeAdapter: StoreAdapter? = null
    var storeAPI: StoreAPI? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater!!.inflate(R.layout.fragment_profile, container, false)



        return itemView
    }

    override fun onClick(v: View) {
        Log.i("ProfileFragment", "OnClick called")
    }

}