package exp.kot.spacex.components.chart

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat


/**
 * Created by Luis Silva on 04/10/2018.
 */
class ChartValueFormatter : IValueFormatter {

    private val mFormat: DecimalFormat = DecimalFormat("###,###,##0")  // use one decimal


    override fun getFormattedValue(
        value: Float,
        entry: Entry,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler
    ): String {
        return mFormat.format(value)
    }
}