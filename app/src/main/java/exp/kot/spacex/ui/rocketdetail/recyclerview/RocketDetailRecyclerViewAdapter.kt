package exp.kot.spacex.ui.rocketdetail.recyclerview

import android.content.Context
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exp.kot.spacex.R
import exp.kot.spacex.components.recyclerview.HeaderItemDecoration
import exp.kot.spacex.model.Launch
import exp.kot.spacex.model.Rocket
import exp.kot.spacex.ui.rocketdetail.recyclerview.stickyheader.Header
import exp.kot.spacex.ui.rocketdetail.recyclerview.viewholders.HeaderViewHolder
import exp.kot.spacex.ui.rocketdetail.recyclerview.viewholders.LaunchViewHolder
import exp.kot.spacex.ui.rocketdetail.recyclerview.viewholders.StaticHeaderViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.rocketdetail_launch_recyclerview_stickyheader_viewholder.view.*

/**
 * Created by Luis Silva on 04/10/2018.
 */

class RocketDetailRecyclerViewAdapter(
    val context: Context,
    val data: MutableList<Launch>,
    val rocket: Rocket
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), HeaderItemDecoration.StickyHeaderInterface {


    private val FIXED_HEADER = 0
    private val HEADER_TYPE = 1
    private val LAUNCH_TYPE = 2

    private val mDiffer = AsyncListDiffer(this, RocketDetailRecyclerViewDiffCallback())
    private val originalData: MutableList<Launch> = data.toMutableList()
    private val showingData: MutableList<Any> = data.toMutableList()

    private var isStaticHeaderLoaded = false
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return FIXED_HEADER
        }
        if (showingData[position] is Launch) {
            return LAUNCH_TYPE
        }
        return HEADER_TYPE
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            FIXED_HEADER ->
                return StaticHeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.rocketdetail_launch_recyclerview_static_header_viewholder,
                        parent,
                        false
                    ) as View, rocket
                )

            HEADER_TYPE ->
                return HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.rocketdetail_launch_recyclerview_header_viewholder,
                        parent,
                        false
                    ) as View
                )

            LAUNCH_TYPE ->
                return LaunchViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.rocketdetail_launch_recyclerview_viewholder,
                        parent,
                        false
                    ) as View
                )

            //we return LaunchViewHolder to return something, a viewHolder
            else ->
                return LaunchViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.rocketdetail_launch_recyclerview_viewholder,
                        parent,
                        false
                    ) as View
                )

        }
    }


    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        disposables.clear()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LaunchViewHolder -> holder.bind(holder.view, showingData[position] as Launch)

            is HeaderViewHolder -> holder.bind(holder.view, showingData[position] as Header)

            is StaticHeaderViewHolder -> {
                if (!isStaticHeaderLoaded) {
                    disposables.clear()
                    holder.bind(holder.view, originalData)
                    isStaticHeaderLoaded = true
                    if (!holder.disposable.isDisposed) {
                        disposables.add(holder.disposable)
                    }
                }
            }
        }
    }


    fun setList(list: List<Launch>) {
        if (!originalData.isEmpty()) {
            isStaticHeaderLoaded = false
            notifyItemChanged(0)
        }
        originalData.clear()
        originalData.addAll(list)

        prepRecyclerData(list)

        mDiffer.submitList(showingData)

    }

    private fun prepRecyclerData(list: List<Launch>) {
        showingData.clear()
        showingData.addAll(list)

        //fixed static header
        showingData.add(0, 0)

        //list of years
        val distints = list.distinctBy { it.launch_year }


        for (launch in distints) {
            showingData.add(showingData.indexOfFirst {
                when (it) {
                    is Launch -> it.launch_year == launch.launch_year
                    else -> false
                }
            }, Header(launch.launch_year))
        }

        //now we have a list with both years and launches objects like for ex. [Header(2006), Launch(1), Launch(2), Header(2007), Launch(3)]
    }

    override fun getItemCount() = showingData.size


    //Sticky Header logic
    override fun getHeaderLayout(headerPosition: Int): Int {
        return R.layout.rocketdetail_launch_recyclerview_stickyheader_viewholder
    }

    override fun bindHeaderData(headerView: View, headerPosition: Int) {
        if (isHeader(headerPosition)) {
            val header: Header = showingData[headerPosition] as Header
            headerView.rocketdetail_recyclerview_stickyheader_viewholder.text = header.year.toString()
        }
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        return if (isHeader(itemPosition)) {
            itemPosition
        } else {
            var headerPosition = 0
            var pos = itemPosition
            do {
                if (isHeader(pos)) {
                    headerPosition = pos
                    break
                }
                pos -= 1
            } while (pos >= 0)
            headerPosition
        }
    }

    override fun isHeader(itemPosition: Int): Boolean {
        return showingData[itemPosition] is Header //is not a Launch then is a header
    }

    override fun hideForPosition(itemPosition: Int): Boolean {
        return itemPosition == 0
    }

}