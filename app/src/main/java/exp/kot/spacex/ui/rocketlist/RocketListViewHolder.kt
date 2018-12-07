package exp.kot.spacex.ui.rocketlist

import android.support.v7.widget.RecyclerView
import android.view.View
import exp.kot.spacex.helpers.hpDebounceClickListener
import exp.kot.spacex.model.Rocket
import exp.kot.spacex.ui.rocketlist.recyclerview.RocketListRecyclerViewAdapter
import kotlinx.android.synthetic.main.rocketlist_recyclerview_viewholder.view.*

/**
 * Created by Luis Silva on 03/10/2018.
 */
class RocketListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(view: View, rocket: Rocket, callback: RocketListRecyclerViewAdapter.AdapterCallBack) {
        view.rocketlist_recyclerview_viewholder_name_input.text = rocket.name
        view.rocketlist_recyclerview_viewholder_country_input.text = rocket.country
        view.rocketlist_recyclerview_viewholder_engines_count_input.text = rocket.engines.number.toString()

        hpDebounceClickListener(view) {
            //we handle any animation or similar here but not navigations
            callback.rocketClick(rocket)

        }


    }


}