package br.com.ramonilho.bolados.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ramonhonorio on 23/08/17.
 */
class BDate {
    companion object {
        fun from(string: String): Date{
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))

            val date = simpleDateFormat.parse(string)
            return date
        }

        fun stringFormattedFrom(string: String): String {
            val date = BDate.from(string)

            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"))

            return simpleDateFormat.format(date)

        }
    }

}