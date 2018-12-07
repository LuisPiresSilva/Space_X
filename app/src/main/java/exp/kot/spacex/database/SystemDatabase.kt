package exp.kot.spacex.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import exp.kot.spacex.database.dao.SystemUserDAO
import exp.kot.spacex.model.SystemUser
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Luis Silva on 06/10/2018.
 *
 * SystemDatabase representing local data, server independent data
 */

@Database(entities = [SystemUser::class], version = 1, exportSchema = false)
abstract class SystemDatabase : RoomDatabase() {

    abstract fun systemUserDao(): SystemUserDAO

    companion object {
        const val DATABASE_NAME = "LOCAL_DATABASE"

        fun getMigrations(): Array<Migration> {
            return arrayOf(
//                MIGRATION_1_2
//                MIGRATION_2_3
            )
        }


//        private val MIGRATION_1_2: Migration = object: Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("")
//            }
//        }


//        private val MIGRATION_2_3: Migration = object: Migration(2, 3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("")
//            }
//        }

    }


    fun insertAsync(obj: Any): Disposable {
        return Single.fromCallable {
            when (obj) {
                is SystemUser -> systemUserDao().insertSync(obj)
            }
        }.subscribeOn(Schedulers.computation()).subscribe()
    }

    fun updateAsync(obj: Any) {
        Single.fromCallable {
            when (obj) {
                is SystemUser -> systemUserDao().updateSync(obj)
            }
        }.subscribeOn(Schedulers.computation()).subscribe()
    }

    fun deleteAsync(obj: Any) {
        Single.fromCallable {
            when (obj) {
                is SystemUser -> systemUserDao().deleteSync(obj)
            }
        }.subscribeOn(Schedulers.computation()).subscribe()
    }


}