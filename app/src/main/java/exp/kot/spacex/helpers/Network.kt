package exp.kot.spacex.helpers

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


/**
 * Created by Luis Silva on 08/10/2018.
 */
class NetworkUtils(internal val app: Application) {


    fun hpHasNetwork(): Boolean {
        return hpHasNetwork(app)
    }

}


fun hpHasNetwork(context: Context): Boolean {
    val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
    return networkInfo?.isConnected == true
}



