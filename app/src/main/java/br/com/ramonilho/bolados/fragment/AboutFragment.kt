package br.com.ramonilho.bolados.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.api.UserAPI
import br.com.ramonilho.bolados.model.User
import br.com.ramonilho.bolados.utils.BDate
import br.com.ramonilho.bolados.utils.BToasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AboutFragment : Fragment() {

    var userAPI: UserAPI = APIUtils.userAPIVersion

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater!!.inflate(R.layout.fragment_about, container, false)

        return itemView
    }

}