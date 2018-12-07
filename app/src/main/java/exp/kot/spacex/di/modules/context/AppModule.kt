package exp.kot.spacex.di.modules.context

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Luis Silva on 05/10/2018.
 */
@Module
class AppModule(var app: Application) {

    @Provides
    @Singleton
    fun app(): Application {
        return app
    }


}