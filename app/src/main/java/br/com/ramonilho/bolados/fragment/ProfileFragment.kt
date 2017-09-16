package br.com.ramonilho.bolados.fragment

import android.content.Intent
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
import br.com.ramonilho.bolados.utils.MapUtils
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import kotlinx.android.synthetic.main.fragment_edit_store.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    var userAPI: UserAPI = APIUtils.userAPIVersion

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater!!.inflate(R.layout.fragment_profile, container, false)

        val creationDate = BDate.stringFormattedFrom(User.shared.creationDate)

        // TextViews
        itemView.tvName.setText(User.shared.name)
        itemView.tvDescription.setText(getString(R.string.profile_creationDate_description) + " " + creationDate)

        if (User.shared.streetAddress.isEmpty()) {
            itemView.tvAddress.setText(getString(R.string.no_address))
        } else {
            itemView.tvAddress.setText(User.shared.streetAddress)
        }

        // EditTexts
        itemView.etName.setText(User.shared.name)
        itemView.etEmail.setText(User.shared.email)

        itemView.btSave.setOnClickListener {
            Log.i("ProfileFragment", "OnSave called FOR REAL")

            val name = itemView.etName.text.toString()
            val email = itemView.etEmail.text.toString()

            val user = User.shared.clone()
            user.name = name
            user.email = email

            Log.i("ProfileFragment", "Username: "+user.name)

            val editCall = userAPI.editInfo(user)
            editCall.enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>?, response: Response<User>?) {

                    Log.i("ProfileFragment", "Request body: "+call!!.request().body())

                    if (response!!.isSuccessful) {
                        Log.i("ProfileFragment", getString(R.string.info_updated))

                        // Saving user into Singleton
                        User.shared = response.body()

                        BToasty.show(getString(R.string.info_updated), context)
                    } else {
                        Log.e("ProfileFragment", "Edit info failed.")
                        BToasty.toastErrorFrom(response.errorBody(), context)
                    }
                }

                override fun onFailure(call: Call<User>?, t: Throwable?) {
                    BToasty.show(getString(R.string.error_request), context)
                }

            })
        }

        itemView.btMap.setOnClickListener {
            try {
                val autocompleteFilter = AutocompleteFilter.Builder()
                        .setTypeFilter(Place.TYPE_COUNTRY)
                        .setCountry("BR")
                        .build()

                val intent = PlaceAutocomplete
                        .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .setFilter(autocompleteFilter)
                        .build(activity)

                startActivityForResult(intent, MapUtils.PLACE_AUTOCOMPLETE_REQUEST_CODE)
            } catch (e: GooglePlayServicesRepairableException) {
                // TODO: Solucionar o erro.
            } catch (e: GooglePlayServicesNotAvailableException) {
                // TODO: Solucionar o erro.
            }
        }

        return itemView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MapUtils.PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            val place = PlaceAutocomplete.getPlace(context, data)
            User.shared.streetAddress = place.address.toString()

            val ft = fragmentManager.beginTransaction()
            ft.detach(this).attach(this).commit()

        }
    }

}