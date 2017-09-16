package br.com.ramonilho.bolados.fragment

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ramonilho.bolados.R
import br.com.ramonilho.bolados.api.APIUtils
import br.com.ramonilho.bolados.model.Store
import br.com.ramonilho.bolados.model.User
import br.com.ramonilho.bolados.utils.BToasty
import br.com.ramonilho.bolados.utils.MapUtils
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import kotlinx.android.synthetic.main.fragment_edit_store.view.*
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.AutocompleteFilter
import kotlinx.android.synthetic.main.fragment_edit_store.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


/**
 * Created by ramonhonorio on 11/09/17.
 */
class StoreCreateFragment : Fragment() {

    var FLAG_CREATE_STORE = "CreateStore"

    var store = Store()
    var location: LatLng? = null

    lateinit var place: Place
    lateinit var itemView: View

    // Container Activity or Fragment must implement this interface
    interface OnCreateStoreListener {
        fun onCreated(store: Store)
    }

    lateinit var mOnCreateStoreListener: OnCreateStoreListener

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        itemView = inflater!!.inflate(R.layout.fragment_edit_store, container, false)

        // Default location: Av Paulista
        location = LatLng(-23.5644, -46.6526)

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

        itemView.btSaveStore.setOnClickListener {
            saveChanges()
        }

        return itemView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == MapUtils.PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                val placeResult = PlaceAutocomplete.getPlace(context, data)
                Log.i(FLAG_CREATE_STORE, "Place: " + placeResult.name)

                location = placeResult.latLng
                place = placeResult

                btMap.text = place.name

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(context, data)
                Log.e(FLAG_CREATE_STORE, status.statusMessage)

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i(FLAG_CREATE_STORE, "Canceled operation")
            }
        }
    }

    fun saveChanges() {
        val storeAPI = APIUtils.storeAPIVersion

        store = Store()
        store.name = etStoreName.text.toString()
        store.description = etStoreDescription.text.toString()
        store.email = etEmail.text.toString()
        store.phone = etPhone.text.toString()
        store.category = spCategory.selectedItem.toString()

        if (place != null) {
            store.addressName = place.name.toString()

            val geocoder = Geocoder(context, Locale.getDefault())
            var addresses: List<Address>? = null
            try {
                addresses = geocoder.getFromLocation(place.latLng.latitude, place.latLng.longitude, 1)
            } catch (exception: Exception) {
                Log.e(FLAG_CREATE_STORE, "GeocodeException: "+exception.localizedMessage)
            }
            if (addresses != null && addresses!!.size > 0) {
                store.city = addresses.get(0).getAddressLine(0)
            }

            if (location != null) {
                store.latitude = location!!.latitude
                store.longitude = location!!.longitude
            }
        }


        store.userId = User.shared.id

        storeAPI.create(store).enqueue(object : Callback<Store> {
            override fun onResponse(call: Call<Store>?, response: Response<Store>?) {
                if (response!!.isSuccessful) {

                    store = response.body()
                    User.shared.storeId = store.id!!
                    Store.fromUser = store

                    if (mOnCreateStoreListener != null)
                        mOnCreateStoreListener.onCreated(store)

                    BToasty.show(getString(R.string.changes_maded), context)

                } else {
                    BToasty.show(getString(R.string.failed_create_store), context)
                }
            }

            override fun onFailure(call: Call<Store>?, t: Throwable?) {
                Log.i(FLAG_CREATE_STORE, "Failed while creating a new store")
                BToasty.show(getString(R.string.failed_create_store), context)
            }

        })

    }

    // In the child fragment.
    fun onAttachToParentFragment(fragment: Fragment) {
        try {
            mOnCreateStoreListener = fragment as OnCreateStoreListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                    fragment.toString() + " must implement OnCreateStoreListener")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(FLAG_CREATE_STORE, "onCreate")
        onAttachToParentFragment(parentFragment)
    }

}