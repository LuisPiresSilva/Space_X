package exp.kot.spacex.ui.rocketdetail.recyclerview.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import exp.kot.spacex.model.Launch
import kotlinx.android.synthetic.main.rocketdetail_launch_recyclerview_viewholder.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Luis Silva on 04/10/2018.
 */

class LaunchViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


    fun bind(view: View, launch: Launch) {
        view.rocketdetail_recyclerview_viewholder_mission_input.text = launch.mission_name

        val date = java.util.Date(launch.launch_date_unix * 1000L)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        sdf.timeZone = TimeZone.getDefault()

        try {
            view.rocketdetail_recyclerview_viewholder_date_input.text = sdf.format(date)
        } catch (e: ParseException) {
//                    send error to somewhere
//                    and
//                    better show something
            e.printStackTrace()
            view.rocketdetail_recyclerview_viewholder_date_input.text = launch.launch_date_local
        }

        if (launch.launch_success != null) {
            view.rocketdetail_recyclerview_viewholder_successful_input.text = "" + launch.launch_success
        } else {
            view.rocketdetail_recyclerview_viewholder_successful_input.text = "not launched yet"
        }
        Glide.with(view.context).load(launch.links.mission_patch)
            .into(view.rocketdetail_recyclerview_viewholder_mission_patch)
    }
}