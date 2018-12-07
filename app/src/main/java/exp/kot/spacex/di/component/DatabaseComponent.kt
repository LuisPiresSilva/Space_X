package exp.kot.spacex.di.component

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

}