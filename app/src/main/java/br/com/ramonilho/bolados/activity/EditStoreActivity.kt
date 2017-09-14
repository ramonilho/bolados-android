package br.com.ramonilho.bolados.activity

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

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
import br.com.ramonilho.bolados.utils.MapUtils
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.model.LatLng
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_edit_store.view.*
import retrofit2.Callback
import android.location.Geocoder
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import br.com.ramonilho.bolados.fragment.StoreFragment
import kotlinx.android.synthetic.main.fragment_store.*
import java.util.*


class EditStoreActivity : AppCompatActivity() {

    val FLAG_STORE_EDIT = "EditStore"

    var storeAPI: StoreAPI? = APIUtils.storeAPIVersion
    var store = Store()

    var location: LatLng? = null
    var place: Place? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_edit_store)

        title = getString(R.string.edit_store_info)

        print("parent: "+parent)
        Log.i(FLAG_STORE_EDIT, "parent: "+parent)
        Log.i(FLAG_STORE_EDIT, "parentActivityIntent: "+parentActivityIntent)
        Log.i(FLAG_STORE_EDIT, "callingActivity: "+callingActivity)

//        onAttachToParentActivity(parent)

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

        btMap.setOnClickListener {
            try {
                val autocompleteFilter = AutocompleteFilter.Builder()
                        .setTypeFilter(Place.TYPE_COUNTRY)
                        .setCountry("BR")
                        .build()

                val intent = PlaceAutocomplete
                        .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                        .setFilter(autocompleteFilter)
                        .build(this)

                startActivityForResult(intent, MapUtils.PLACE_AUTOCOMPLETE_REQUEST_CODE)
            } catch (e: GooglePlayServicesRepairableException) {
                // TODO: Solucionar o erro.
            } catch (e: GooglePlayServicesNotAvailableException) {
                // TODO: Solucionar o erro.
            }
        }

        btSaveStore.setOnClickListener {
            saveChanges()
        }
    }

    fun updateFields() {
        etStoreName.setText(store.name)
        etStoreDescription.setText(store.description)

        val array_spinner = spCategory.adapter as ArrayAdapter<String>
        spCategory.setSelection(array_spinner.getPosition(store.category))

        etEmail.setText(store.email)
        etPhone.setText(store.phone)

        btMap.setText(store.addressName)

//        // Loading showcase images
//        // It's not the prettiest but I'm gonna keep it
//        if (store.pictures.size > 1) {
//            ivShowcaseImage1.load(APIUtils.BASE_URL + store.pictures[0]) { p ->
//                p.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fit().centerCrop()
//                        .transform(RoundedCornersTransformation(10, 10))
//            }
//            ivShowcaseImage1.imageTintList = null
//        }
//
//        if (store.pictures.size > 2) {
//            ivShowcaseImage2.load(APIUtils.BASE_URL + store.pictures[1]) { p ->
//                p.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fit().centerCrop()
//                        .transform(RoundedCornersTransformation(10, 10))
//            }
//            ivShowcaseImage2.imageTintList = null
//        }
//
//        if (store.pictures.size > 3) {
//            ivShowcaseImage3.load(APIUtils.BASE_URL + store.pictures[2]) { p ->
//                p.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fit().centerCrop()
//                        .transform(RoundedCornersTransformation(10, 10))
//            }
//            ivShowcaseImage3.imageTintList = null
//        }
//
//        if (store.pictures.size > 4) {
//            ivShowcaseImage4.load(APIUtils.BASE_URL + store.pictures[3]) { p ->
//                p.placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).fit().centerCrop()
//                        .transform(RoundedCornersTransformation(10, 10))
//            }
//            ivShowcaseImage4.imageTintList = null
//        }
    }

    fun saveChanges() {
        Log.i("EditStore", "Saving changes...")

        store.name = etStoreName.text.toString()
        store.description = etStoreDescription.text.toString()
        store.email = etEmail.text.toString()
        store.phone = etPhone.text.toString()
        store.category = spCategory.selectedItem.toString()

        if (place != null) {
            store.addressName = place!!.name.toString()

            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(location!!.latitude, location!!.longitude, 1)
            store.city = addresses[0].getAddressLine(0)
        }

        if (location != null) {
            store.latitude = location!!.latitude
            store.longitude = location!!.longitude
        }

        store.userId = User.shared.id

        storeAPI!!.editInfo(store).enqueue(object : retrofit2.Callback<Store> {
            override fun onFailure(call: Call<Store>?, t: Throwable?) {
                BToasty.show(getString(R.string.error_request), baseContext)
            }

            override fun onResponse(call: Call<Store>?, response: Response<Store>?) {
                if (response!!.isSuccessful) {
                    store = response.body()

                    updateFields()
                    BToasty.show(getString(R.string.changes_maded), baseContext)

//                    mOnEditedListener.onEdited(store)
                    setResult(0)
                    finish()
                } else {
                    Log.e("ProfileFragment", "Edit info failed.")
                    BToasty.toastErrorFrom(response.errorBody(), baseContext)
                }
            }

        })
    }

    // Container Activity or Fragment must implement this interface
    interface OnEditedListener {
        fun onEdited(store: Store)
    }

    lateinit var mOnEditedListener: OnEditedListener

    // In the child activity.
    fun onAttachToParentActivity(activity: Activity) {
        print("onAttachEditStore: "+activity.toString())
        try {
            mOnEditedListener = activity as MainActivity
        } catch (e: ClassCastException) {
            throw ClassCastException(
                    activity.toString() + " must implement OnCreateStoreListener")
        }
    }
}
