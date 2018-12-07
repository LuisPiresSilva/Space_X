@file:Suppress("AndroidUnresolvedRoomSqlReference")

package exp.kot.spacex.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import exp.kot.spacex.model.SystemUser

/**
 * Created by Luis Silva on 06/10/2018.
 */
@Dao
abstract class SystemUserDAO : BaseDAO<SystemUser> {


    @Query("SELECT * FROM SystemUser WHERE id = ${SystemUser.globalID}")
    abstract fun getUserSystemSync(): SystemUser

    @Query("SELECT * FROM SystemUser WHERE id = ${SystemUser.globalID}")
    abstract fun getUserSystem(): LiveData<SystemUser>

    @Query("SELECT * FROM SystemUser")
    abstract fun getAllUserSystemSync(): List<SystemUser>

    @Query("SELECT * FROM SystemUser")
    abstract fun getAllUserSystem(): LiveData<List<SystemUser>>


    //there should be only one system user with id 'globalID' and should not be deleted only changed
    override fun deleteSync(obj: SystemUser) {}


}