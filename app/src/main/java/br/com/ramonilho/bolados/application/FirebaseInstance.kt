package br.com.ramonilho.bolados.application

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.iid.FirebaseInstanceId



/**
 * Created by ramonhonorio on 16/09/17.
 */
class FirebaseInstance : FirebaseInstanceIdService() {
    val TAG = FirebaseInstance::class.java.simpleName!!

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        Log.d(TAG, "Token refreshed!")
        val refreshedToken = FirebaseInstanceId.getInstance().token

        refreshedToken?.let {
            sendRegistrationToServer(it)
        }
    }

    private fun sendRegistrationToServer(refreshedToken: String) {
        Log.d(TAG, "Refreshed token: $refreshedToken - send this to server?")
    }

}