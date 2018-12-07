package exp.kot.spacex.model

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable


/**
 * Created by Luis Silva on 03/10/2018.
 */
@Entity(tableName = "engine")
data class Engine
    (
    @PrimaryKey val number: Int
) : Parcelable {


    constructor(parcel: Parcel) : this(parcel.readInt())


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(number)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        @JvmField
        val CREATOR = object : Parcelable.Creator<Engine> {
            override fun createFromParcel(parcel: Parcel): Engine {
                return Engine(parcel)
            }

            override fun newArray(size: Int): Array<Engine?> {
                return arrayOfNulls(size)
            }
        }

    }


    @Dao
    interface EngineDataDAO {
        @Query("SELECT * from engine")
        fun getAll(): List<Engine>


        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(vararg engines: Engine)


        @Delete
        fun delete(vararg engines: Engine)

        @Query("DELETE FROM engine")
        fun deleteAll()

    }
}