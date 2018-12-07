package exp.kot.spacex.ui.rocketlist.recyclerview

import android.support.v7.util.DiffUtil
import exp.kot.spacex.model.Rocket

/**
 * Created by Luis Silva on 04/12/2018.
 */

class RocketListRecyclerViewDiffCallback : DiffUtil.ItemCallback<Rocket>() {

    override fun areItemsTheSame(oldItem: Rocket, newItem: Rocket): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Rocket, newItem: Rocket): Boolean {
        return oldItem == newItem
    }
}