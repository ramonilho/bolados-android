package br.com.ramonilho.bolados.extensions

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

/**
 * Created by ramonhonorio on 02/09/17.
 * Extensions for ImageView - Picasso
 */

val Context.picasso: Picasso
    get() = Picasso.with(this)

fun ImageView.load(path: String, request: (RequestCreator) -> RequestCreator) {
    request(getContext().picasso.load(path)).into(this)
}