package exp.kot.spacex.ui.rocketdetail.recyclerview.viewholders

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import exp.kot.spacex.components.chart.AxesValueFormatter
import exp.kot.spacex.components.chart.ChartValueFormatter
import exp.kot.spacex.model.Launch
import exp.kot.spacex.model.Rocket
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.rocketdetail_launch_recyclerview_static_header_viewholder.view.*
import java.util.*

/**
 * Created by Luis Silva on 04/10/2018.
 */

class StaticHeaderViewHolder(val view: View, val rocket: Rocket) : RecyclerView.ViewHolder(view) {

    lateinit var disposable: Disposable

    @SuppressLint("CheckResult")
    fun bind(view: View, list: List<Launch>) {
        disposable = Single.fromCallable {
            prepareGraphData(list)
        }.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { sortedGraphData ->

                if (sortedGraphData.isNotEmpty()) {
                    val entries = ArrayList<Entry>()
                    for (data in sortedGraphData) {
                        entries.add(Entry(data.key.toFloat(), data.value.toFloat()))
                    }

                    val min = sortedGraphData.minBy { it.key.toFloat() }?.key
                    val max = sortedGraphData.maxBy { it.key.toFloat() }?.key


                    val dataSet = LineDataSet(entries, "Launches per year")
                    dataSet.setDrawFilled(true)

                    //simplify data values format
                    val lineData = LineData(dataSet)
                    lineData.setValueFormatter(ChartValueFormatter())

                    //increase min and max years by 1 so that graph does not start in edge of screen
                    // and set y min to 0 to have a common comparison value
                    if (min != null && max != null) {
                        view.rocketdetail_chart.xAxis.axisMinimum = min - 1F
                        view.rocketdetail_chart.xAxis.axisMaximum = max + 1F
                    }
                    view.rocketdetail_chart.axisLeft.axisMinimum = 0F


                    //remove excess information
                    view.rocketdetail_chart.axisRight.isEnabled = false
                    val description = Description()
                    description.text = ""
                    view.rocketdetail_chart.description = description


                    //simplify axes values format
                    view.rocketdetail_chart.axisLeft.granularity = 1F
                    view.rocketdetail_chart.axisLeft.isGranularityEnabled = true

                    view.rocketdetail_chart.xAxis.granularity = 1F
                    view.rocketdetail_chart.xAxis.isGranularityEnabled = true

                    view.rocketdetail_chart.xAxis.valueFormatter = AxesValueFormatter()
                    view.rocketdetail_chart.axisLeft.valueFormatter = AxesValueFormatter()


                    view.rocketdetail_chart.setDrawGridBackground(true)


                    view.rocketdetail_chart.animateY(2000)
                    view.rocketdetail_chart.data = lineData
                    view.rocketdetail_chart.invalidate() // refresh
                } else {

                }
            }

        //chart does not seem to accept top margin or padding in xml so we add here
        //so it has some space form top edge of screen
        view.rocketdetail_chart.extraTopOffset = 30F

        view.rocketdetail_description.text = rocket.description

    }


    private fun prepareGraphData(launches: List<Launch>): SortedMap<Int, Int> {
        val graphData: HashMap<Int, Int> = HashMap()
        for (data in launches) {
            val yearLaunch = graphData[data.launch_year]
            when {
                yearLaunch != null -> graphData.put(data.launch_year, yearLaunch + 1)
                else -> graphData.put(data.launch_year, 1)
            }
            if (disposable.isDisposed) {
                graphData.clear()
                break
            }
        }

        //we sort the data so that the line graph does not have crossed lines
        return graphData.toSortedMap()
    }

}