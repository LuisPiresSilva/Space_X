package exp.kot.spacex.helpers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created by Luis Silva on 18/10/2018.
 */


fun launchAsync(block: suspend CoroutineScope.() -> Unit): Job {
    return GlobalScope.launch(Main) { block() }
}