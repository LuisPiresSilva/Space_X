package exp.kot.spacex.helpers

import okhttp3.OkHttpClient

/**
 * Created by Luis Silva on 10/10/2018.
 */

/**
 * helper function to cancel all requests
 */
fun OkHttpClient.hpCancelAllRequests() {
    dispatcher().cancelAll()
}


/**
 * helper function to cancel all requests of a given tag
 */
fun OkHttpClient.hpCancelAllRequests(tag: String) {
    dispatcher().queuedCalls().forEach { call ->
        if (call.request().tag() == tag) {
            call.cancel()
        }
    }
    dispatcher().runningCalls().forEach { call ->
        if (call.request().tag() == tag) {
            call.cancel()
        }
    }

}