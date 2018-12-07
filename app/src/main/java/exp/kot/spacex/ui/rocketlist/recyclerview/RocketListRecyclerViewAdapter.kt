package exp.kot.spacex.ui.rocketlist.recyclerview

import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exp.kot.spacex.R
import exp.kot.spacex.model.Rocket
import exp.kot.spacex.ui.rocketlist.RocketListViewHolder


/**
 * Created by Luis Silva on 03/10/2018.
 */

class RocketListRecyclerViewAdapter(val data: MutableList<Rocket>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface AdapterCallBack {
        fun rocketClick(rocket: Rocket)
        fun rocketLongClick(rocket: Rocket)
    }

    private lateinit var callback: AdapterCallBack
    fun sendCallBacksTo(callback: AdapterCallBack) {
        this.callback = callback
    }


    private val mDiffer = AsyncListDiffer(this, RocketListRecyclerViewDiffCallback())
    private val originalData: MutableList<Rocket> = data.toMutableList()
    private var showingData: MutableList<Rocket> = data.toMutableList()
    private var filter: Boolean = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RocketListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rocketlist_recyclerview_viewholder,
                parent,
                false
            ) as View
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RocketListViewHolder -> holder.bind(holder.view, showingData[position], callback)
        }
    }


    override fun getItemCount() = showingData.size

    fun setList(list: List<Rocket>) {
        originalData.clear()
        originalData.addAll(list)

        showingData.clear()

        filterList(filter)
    }


    fun filterList(onlyActive: Boolean) {
        filter = onlyActive


        //in case of rotation filter might be on and showingData is empty so we must filter over the original
        showingData = if (filterDeeper() && !showingData.isEmpty()) {
            showingData.asSequence().filter {
                if (onlyActive)
                    it.active == filter
                else
                    true
            }.toMutableList()
        } else {
            originalData.asSequence().filter {
                if (onlyActive)
                    it.active == filter
                else
                    true
            }.toMutableList()
        }

//       this is used for rotation handling when filter is on
        if (showingData.isEmpty()) {
            notifyDataSetChanged()
        } else {
            mDiffer.submitList(showingData)
        }


    }


    //not needed for this use case but this function would check if we can filter over an already filtered list
    //or if we must use the original data
    private fun filterDeeper(): Boolean {
        return filter
    }

}


