package br.com.ramonilho.bolados.utils

import android.content.Context
import android.widget.Toast
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * Created by ramonhonorio on 21/08/17.
 */
class BToasty {
    companion object {
        fun toastErrorFrom(response: ResponseBody, context: Context) {
            try {
                val jObjError = JSONObject(response.string())
                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }

        fun show(message: String, context: Context) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}