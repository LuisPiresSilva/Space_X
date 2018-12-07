package exp.kot.spacex.di

import exp.kot.spacex.helpers.hpBuildHost
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by Luis Silva on 05/10/2018.
 */

class NetworkWrapper(
    val retrofitBuilder: Retrofit.Builder,
    val okhttpclient: OkHttpClient
) {

    lateinit var retrofit: Retrofit

    fun buildHost(host: String): NetworkWrapper {
        retrofit = retrofitBuilder.hpBuildHost(host)
        return this
    }
}