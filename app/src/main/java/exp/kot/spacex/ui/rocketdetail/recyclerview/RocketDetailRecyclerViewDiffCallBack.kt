package exp.kot.spacex.ui.rocketdetail.recyclerview

import android.support.v7.util.DiffUtil
import exp.kot.spacex.model.Launch
import exp.kot.spacex.ui.rocketdetail.recyclerview.stickyheader.Header

/**
 * Created by Luis Silva on 04/10/2018.
 */

class RocketDetailRecyclerViewDiffCallback : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is Launch && newItem is Launch -> oldItem.id == newItem.id

            oldItem is Header && newItem is Header -> oldItem.year == newItem.year

            oldItem is Int && newItem is Int -> oldItem == newItem

            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }
}