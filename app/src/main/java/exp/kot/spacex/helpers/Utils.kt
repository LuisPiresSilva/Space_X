package exp.kot.spacex.helpers

import  android.app.Application
import javax.inject.Singleton

/**
 * Created by Luis Silva on 07/10/2018.
 *
 * helper class that links several utility classes
 */
@Singleton
class Utils(internal val app: Application) {

    init {
        network = NetworkUtils(app)
    }

    companion object {
        lateinit var network: NetworkUtils
    }
}