package exp.kot.spacex.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Luis Silva on 04/10/2018.
 */
data class Launch
    (
    @SerializedName("flight_number") val id: Int,
    val mission_name: String,
    val launch_year: Int,
    val name: String,
    val launch_date_unix: Long,
    val launch_date_local: String,
    val launch_success: Boolean?, //can be null
    val links: Link
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readParcelable(Link::class.java.classLoader)
    )




    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(mission_name)
        parcel.writeInt(launch_year)
        parcel.writeString(name)
        parcel.writeLong(launch_date_unix)
        parcel.writeString(launch_date_local)
        parcel.writeValue(launch_success)
        parcel.writeParcelable(links, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Launch> {
        override fun createFromParcel(parcel: Parcel): Launch {
            return Launch(parcel)
        }

        override fun newArray(size: Int): Array<Launch?> {
            return arrayOfNulls(size)
        }
    }


}