package exp.kot.spacex.di.modules.persistence

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import dagger.Module
import dagger.Provides
import exp.kot.spacex.SpaceXApplication
import exp.kot.spacex.database.SystemDatabase
import exp.kot.spacex.model.SystemUser
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton


/**
 * Created by Luis Silva on 06/10/2018.
 */
@Module
class RoomModule {

    //must create a singleton that controls DB open/close states
    @Provides
    @Singleton
    fun roomLocalSystemDatabase(app: Application): SystemDatabase {
        return Room.databaseBuilder(app, SystemDatabase::class.java, SystemDatabase.DATABASE_NAME)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Single.fromCallable {
                        SpaceXApplication.database().openLocalSystemDatabase().insertAsync(SystemUser())
                    }.subscribeOn(Schedulers.computation()).subscribe()
                }
            })
            .addMigrations(*SystemDatabase.getMigrations())
            .build()
    }


}