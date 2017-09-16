/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.realm.internal.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import java.util.concurrent.CopyOnWriteArrayList

import io.realm.internal.Util

/**
 * Adicionei essa classe porque o Realm estava crashando por causa da conexão
 */
class NetworkStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val connected = isOnline(context)
        for (listener in listeners) {
            listener.onChange(connected)
        }
    }

    interface ConnectionListener {
        fun onChange(connectionAvailable: Boolean)
    }

    companion object {

        private val listeners = CopyOnWriteArrayList<ConnectionListener>()

        /**
         * Add a listener to be notified about any network changes.
         * This method is thread safe.
         *
         *
         * IMPORTANT: Not removing it again will result in major leaks.
         *
         * @param listener the listener.
         */
        fun addListener(listener: ConnectionListener) {
            listeners.add(listener)
        }

        /**
         * Removes a network listener.
         * This method is thread safe.
         *
         * @param listener the listener.
         */
        @Synchronized
        fun removeListener(listener: ConnectionListener) {
            listeners.remove(listener)
        }

        /**
         * Attempt to detect if a device is online and can transmit or receive data.
         * This method is thread safe.
         *
         *
         * An emulator is always considered online, as `getActiveNetworkInfo()` does not report the correct value.
         *
         * @param context an Android context.
         * @return `true` if device is online, otherwise `false`.
         */
        fun isOnline(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting || Util.isEmulator()
        }
    }
}