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
import br.com.ramonilho.bolados.adapter.StoreAdapter
import br.com.ramonilho.bolados.api.StoreAPI
import kotlinx.android.synthetic.main.fragment_edit_store.view.*
import kotlinx.android.synthetic.main.fragment_store.view.*
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter


class StoreFragment : Fragment(), View.OnClickListener {

    var storeAdapter: StoreAdapter? = null
    var storeAPI: StoreAPI? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater!!.inflate(R.layout.fragment_store, container, false)


        // Edit button action
        itemView.ibEditStore.setOnClickListener(View.OnClickListener {

            val intent = Intent(activity, EditStoreActivity::class.java)
            startActivity(intent)

        })


        return itemView
    }

    override fun onClick(v: View) {
        Log.i("ProfileFragment", "OnClick called")
    }

}