package br.com.ramonilho.bolados.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.api.StoreAPI
import br.com.ramonilho.bolados.model.Store
import br.com.ramonilho.bolados.model.User
import br.com.ramonilho.bolados.utils.BToasty
import android.os.StrictMode
import br.com.ramonilho.bolados.utils.MapUtils


/**
 * Created by ramonhonorio on 11/09/17.
 */
class StoreBaseFragment: Fragment(), StoreCreateFragment.OnCreateStoreListener {

    val BASE_STORE_FRAGMENT = "StoreBase"

    var storeAPI: StoreAPI? = APIUtils.storeAPIVersion
    var store = Store()
    var currentFragIndex = 0
    lateinit var currentFragment: Fragment

    lateinit var itemView: View

    lateinit var mOnCreateStoreListener: StoreCreateFragment.OnCreateStoreListener

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        if (User.shared.storeId != 0) {
            val response = storeAPI!!.store(User.shared.storeId).execute()
            if (response!!.isSuccessful) {
                store = response.body()
                currentFragIndex = 0

            } else {
                Log.e("StoreBaseFragment", "Failed to get store.")
                BToasty.toastErrorFrom(response.errorBody(), context)

                currentFragIndex = 1
            }
        }

        val ft = childFragmentManager.beginTransaction()

        var fragment: Fragment? = null
        when (currentFragIndex) {
            0 -> fragment = StoreFragment()
            1 -> fragment = StoreCreateFragment()
        }

        currentFragment = fragment!!

        MapUtils.shouldUseMark = currentFragIndex == 1

        ft.add(fragment, BASE_STORE_FRAGMENT)

        ft.replace(R.id.storeContainer, fragment)
        ft.commit()

        return inflater!!.inflate(R.layout.fragment_base_store, container, false)
    }

    // In the child fragment.
    fun onAttachToParentFragment(fragment: Fragment) {
        try {
            mOnCreateStoreListener = fragment as StoreCreateFragment.OnCreateStoreListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                    fragment.toString() + " must implement OnCreateStoreListener")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("StoreBase", "onCreate")

        if (parentFragment != null) {
            onAttachToParentFragment(parentFragment)
        } else {
            mOnCreateStoreListener = activity as StoreCreateFragment.OnCreateStoreListener
        }

    }

    override fun onCreated(store: Store) {
        Log.i("StoreBase", "onCreatedStore")
        mOnCreateStoreListener.onCreated(store)
    }

}