package exp.kot.spacex

import android.app.Application
import exp.kot.spacex.di.component.DaggerDatabaseComponent
import exp.kot.spacex.di.component.DaggerNetworkComponent
import exp.kot.spacex.di.component.DatabaseComponent
import exp.kot.spacex.di.component.NetworkComponent
import exp.kot.spacex.helpers.Utils
import timber.log.Timber


/**
 * Created by Luis Silva on 05/10/2018.
 */

class SpaceXApplication : Application() {

    companion object {
        lateinit var app: SpaceXApplication
        fun network(): NetworkComponent {
            return app.networkComponent
        }

        fun database(): DatabaseComponent {
            return app.databaseComponent
        }
    }


    private lateinit var networkComponent: NetworkComponent
    private lateinit var databaseComponent: DatabaseComponent

    override fun onCreate() {
        super.onCreate()

        app = this

        //initialise global components
        Utils(app)

        networkComponent = DaggerNetworkComponent.builder()
            .applicationContext(app)
            .build()


        databaseComponent = DaggerDatabaseComponent.builder()
            .applicationContext(app)
            .build()




        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }


//        if (com.squareup.leakcanary.LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        // Normal app init code...
//        com.squareup.leakcanary.LeakCanary.install(this)
    }


}