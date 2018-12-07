package exp.kot.spacex.ui.rocketdetail.recyclerview.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import exp.kot.spacex.ui.rocketdetail.recyclerview.stickyheader.Header
import kotlinx.android.synthetic.main.rocketdetail_launch_recyclerview_header_viewholder.view.*

/**
 * Created by Luis Silva on 04/10/2018.
 */

class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


    fun bind(view: View, header: Header) {
        view.rocketdetail_recyclerview_header_viewholder.text = "" + header.year
    }
}