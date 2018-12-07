package exp.kot.spacex.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Luis Silva on 03/10/2018.
 */

data class Rocket
    (
    val id: Int,
    val active: Boolean,
    @SerializedName("rocket_id") val rocketID: String,
    @SerializedName("rocket_name") val name: String,
    val country: String,
    val engines: Engine,
    val description: String
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Engine::class.java.classLoader),
        parcel.readString()
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeByte(if (active) 1 else 0)
        parcel.writeString(rocketID)
        parcel.writeString(name)
        parcel.writeString(country)
        parcel.writeParcelable(engines, flags)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        const val TAG_PARCELABLE_ROCKET = "TAG_PARCELABLE_ROCKET"

        @JvmField
        val CREATOR = object : Parcelable.Creator<Rocket> {
            override fun createFromParcel(parcel: Parcel): Rocket {
                return Rocket(parcel)
            }

            override fun newArray(size: Int): Array<Rocket?> {
                return arrayOfNulls(size)
            }
        }
    }


}