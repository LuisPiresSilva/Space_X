package exp.kot.spacex.helpers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Created by Luis Silva on 08/10/2018.
 */


/**
 * helper function to add a tag to a request
 */
fun <T> Call<T>.hpAddTag(tag: String) {
    request().newBuilder().tag(tag)
}


/**
 * helper function to enqueue request with a tag
 */
fun <T> Call<T>.hpEnqueveWithTag(tag: String, callback: Callback<T>) {
    hpAddTag(tag)
    enqueue(callback)
}


/**
 * helper function to enqueue request with reduced boilerplate code
 *
 * &#8203;
 *
 *  example:
 * ```
 * .hpEnqueve(
 *      OnSuccess = { rCall, response ->
 *          code here for success
 *      },
 *      OnFailure = { rCall, t ->
 *          code here for failure
 *      }
 * )
 * ```
 */
fun <T> Call<T>.hpEnqueve(OnSuccess: (Call<T>, Response<T>) -> Unit, OnFailure: (Call<T>, t: Throwable) -> Unit) {
    this.enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>?, t: Throwable?) {
            OnFailure(call!!, t!!)
        }

        override fun onResponse(call: Call<T>?, response: Response<T>) {
            OnSuccess(call!!, response)
        }
    })
}


/**
 * helper function to enqueue request with reduced boilerplate code that also accepts tag with a tag
 *
 * &#8203;
 *
 * example:
 * ```
 * .hpEnqueveWithTag("TAG",
 *      OnSuccess = { rCall, response ->
 *          code here for success
 *      },
 *      OnFailure = { rCall, t ->
 *          code here for failure
 *      }
 * )
 * ```
 */
fun <T> Call<T>.hpEnqueveWithTag(
    tag: String,
    OnSuccess: (Call<T>, Response<T>) -> Unit,
    OnFailure: (Call<T>, t: Throwable) -> Unit
) {
    hpAddTag(tag)
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>?, t: Throwable?) {
            OnFailure(call!!, t!!)
        }

        override fun onResponse(call: Call<T>?, response: Response<T>) {
            OnSuccess(call!!, response)
        }
    })
}


/**
 * helper function that checks if is a cached response
 */
fun <T> Response<T>.hpIsFromCache(): Boolean {
    return raw().networkResponse() == null
}


/**
 * helper function to create a retrofit instance for a certain host
 */
fun Retrofit.Builder.hpBuildHost(host: String): Retrofit {
    return baseUrl(host).build()
}