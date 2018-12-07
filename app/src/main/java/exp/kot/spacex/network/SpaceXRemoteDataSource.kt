package exp.kot.spacex.network

import exp.kot.spacex.SpaceXApplication
import exp.kot.spacex.di.NetworkWrapper
import exp.kot.spacex.helpers.Utils
import exp.kot.spacex.helpers.hpCancelAllRequests
import exp.kot.spacex.helpers.hpEnqueveWithTag
import exp.kot.spacex.helpers.hpIsFromCache
import exp.kot.spacex.model.Launch
import exp.kot.spacex.model.Rocket
import retrofit2.Call


/**
 * Created by Luis Silva on 03/10/2018.
 */

class SpaceXRemoteDataSource {


    private val api: SpaceXAPI
    private val host = "https://api.spacexdata.com"

    private val network: NetworkWrapper =
        SpaceXApplication.networkComponent().prepareRetrofitNonCaching().buildHost(host)

    private var requests: Set<String> = mutableSetOf()

    init {
        this.api = network.retrofit.create(SpaceXAPI::class.java)
    }

    //handling of requests (params logics etc)
    private fun getAllRockets(): Call<List<Rocket>> {
        return api.getRockets()
    }

    private fun getAllRocketLaunches(rocketID: String): Call<List<Launch>> {
        return api.getRocketLaunches(rocketID)
    }


    fun cancelRequests() {
        requests.forEach {
            network.okhttpclient.hpCancelAllRequests(it)
        }
        requests = mutableSetOf()
    }

    fun cancelRequests(tag: String) {
        network.okhttpclient.hpCancelAllRequests(tag)
        requests.minus(tag)
    }


    //request and pass Objects and Error
    fun getAllRockets(result: (List<Rocket>?) -> Unit, error: (Error?) -> Unit, tag: String = "") {
        requests.takeIf { !tag.isEmpty() }?.plus(tag)

        getAllRockets().hpEnqueveWithTag(tag,
            OnSuccess = { rCall, response ->
                when {
                    response.isSuccessful -> {
                        result(response.body())
                    }
                    else -> {
                        if (response.hpIsFromCache() && !Utils.network.hpHasNetwork()) {
                            error(Error("no internet"))
                        }
                    }
                }

            },
            OnFailure = { rCall, t ->
                error(Error(t.message))
            }
        )
    }

    //request and pass Objects and Error
    fun getAllRocketLaunches(
        rocketID: String,
        result: (List<Launch>?) -> Unit,
        error: (Error?) -> Unit,
        tag: String = ""
    ) {
        requests.takeIf { !tag.isEmpty() }?.plus(tag)

        getAllRocketLaunches(rocketID).hpEnqueveWithTag(tag,
            OnSuccess = { rCall, response ->
                when {
                    response.isSuccessful -> result(response.body())
                    else -> {
                        if (response.hpIsFromCache() && !Utils.network.hpHasNetwork()) {
                            error(Error("no internet"))
                        }
                    }
                }
            },
            OnFailure = { rCall, t ->
                error(Error(t.message))
            }
        )
    }


}