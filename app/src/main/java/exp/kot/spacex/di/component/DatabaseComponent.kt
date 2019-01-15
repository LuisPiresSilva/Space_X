package exp.kot.spacex.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import exp.kot.spacex.database.SystemDatabase
import exp.kot.spacex.di.modules.persistence.RoomModule
import javax.inject.Singleton

/**
 * Created by Luis Silva on 06/10/2018.
 */
@Singleton
@Component(modules = [RoomModule::class])
interface DatabaseComponent {

    fun openLocalSystemDatabase(): SystemDatabase


//    repeated code but is preferable to writting a new module for context
//    docs: @BindsInstance methods should be preferred to writing a @Module with constructor arguments
//    and immediately providing those values.
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Application): Builder

        fun build(): DatabaseComponent
    }
}