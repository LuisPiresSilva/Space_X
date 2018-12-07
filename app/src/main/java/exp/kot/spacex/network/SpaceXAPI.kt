package exp.kot.spacex.network

import exp.kot.spacex.model.Launch
import exp.kot.spacex.model.Rocket
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Luis Silva on 03/10/2018.
 */
interface SpaceXAPI {


    @GET("/v3/rockets")
    fun getRockets(): Call<List<Rocket>>


    @GET("/v3/rockets/{ID}")
    fun getRocket(@Path("Id") rocketID: String): Call<Rocket>

    @GET("/v3/launches")
    fun getRocketLaunches(@Query("rocket_id") rocketID: String): Call<List<Launch>>
}