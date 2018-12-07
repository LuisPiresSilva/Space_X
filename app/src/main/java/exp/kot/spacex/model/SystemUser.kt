package exp.kot.spacex.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Luis Silva on 05/10/2018.
 */

@Entity
data class SystemUser(
    @ColumnInfo(name = "first_time") var firstTime: Boolean = true
) {

    @PrimaryKey
    var id: Int = globalID
        set(value) {
            field = globalID
        }

    companion object {
        const val globalID = 1
    }

}



