package exp.kot.spacex.database.dao

import android.arch.persistence.room.*

/**
 * Created by Luis Silva on 06/10/2018.
 */

@Dao
interface BaseDAO<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSync(obj: T)

    @Update
    fun updateSync(obj: T)

    @Delete
    fun deleteSync(obj: T)


}