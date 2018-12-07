package exp.kot.spacex.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Luis Silva on 04/10/2018.
 */
data class Link
    (
    val mission_patch: String,
    val mission_patch_small: String
) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mission_patch)
        parcel.writeString(mission_patch_small)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Link> {
        override fun createFromParcel(parcel: Parcel): Link {
            return Link(parcel)
        }

        override fun newArray(size: Int): Array<Link?> {
            return arrayOfNulls(size)
        }
    }


}