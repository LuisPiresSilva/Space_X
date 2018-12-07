package exp.kot.spacex.helpers

import android.view.View
import com.jakewharton.rxbinding.view.RxView
import java.util.concurrent.TimeUnit

/**
 * Created by Luis Silva on 12/10/2018.
 */


/**
 * helper function to avoid multiple clicks in a view
 */
fun hpDebounceClickListener(view: View, func: () -> Unit) {
    RxView.clicks(view).throttleFirst(750, TimeUnit.MILLISECONDS).subscribe {
        func()
    }
}
