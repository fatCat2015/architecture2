package com.eju.tools.initializer

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.startup.Initializer
import timber.log.Timber

class NetworkInitializer: SimpleInitializer<Unit>()  {

    override fun create(context: Context) {
        init(context)
    }

    private fun init(context: Context) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val callback = object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
                networkCapabilities?.let { networkCapabilities ->
                    onNetworkStateChanged(
                        getConnectedNetworkState(
                            networkCapabilities
                        )
                    )
                } ?: onNetworkStateChanged(
                    NetworkState.OTHER
                )
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                onNetworkStateChanged(
                    NetworkState.NONE
                )
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                onNetworkStateChanged(
                    getConnectedNetworkState(
                        networkCapabilities
                    )
                )
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(callback)
        } else {
            connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), callback)
        }
    }

    private fun getConnectedNetworkState(networkCapabilities: NetworkCapabilities): NetworkState {
        return when{
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkState.WIFI
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkState.MOBILE
            else -> NetworkState.OTHER
        }
    }


    private fun onNetworkStateChanged(newNetworkState: NetworkState){
        if(newNetworkState!= networkState){
            Timber.i("onNetworkStateChanged oldState:$networkState newState:${newNetworkState} ")
            _networkState = newNetworkState
            networkStateLiveData.postValue(newNetworkState)
        }
    }


}


enum class NetworkState{
    NONE,
    WIFI,
    MOBILE,
    OTHER,
}

internal var _networkState = NetworkState.NONE

internal val networkStateLiveData = MutableLiveData<NetworkState>()


//以下是对外方法
val networkState : NetworkState get() = _networkState

val networkConnected :Boolean get() = _networkState != NetworkState.NONE

fun ComponentActivity.observeNetworkStatus(onStateChanged:(Boolean, NetworkState)->Unit){
    networkStateLiveData.observe(this){
        onStateChanged.invoke(networkConnected,it)
    }
}

fun Fragment.observeNetworkStatus(onStateChanged:(Boolean, NetworkState)->Unit){
    networkStateLiveData.observe(this){
        onStateChanged.invoke(networkConnected,it)
    }
}

