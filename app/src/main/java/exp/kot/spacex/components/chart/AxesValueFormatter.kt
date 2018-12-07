package exp.kot.spacex.components.chart

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.DecimalFormat


/**
 * Created by Luis Silva on 04/10/2018.
 */
class AxesValueFormatter : IAxisValueFormatter {


    private val mFormat: DecimalFormat = DecimalFormat("###") // no decimal


    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return mFormat.format(value)
    }
}