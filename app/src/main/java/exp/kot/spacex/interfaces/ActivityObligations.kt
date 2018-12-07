package exp.kot.spacex.interfaces

import android.content.Intent
import android.support.v4.app.Fragment

/**
 * Created by Luis Silva on 04/10/2018.
 */

interface ActivityObligations {

    fun navigate(caller: Fragment, intent: Intent)

}