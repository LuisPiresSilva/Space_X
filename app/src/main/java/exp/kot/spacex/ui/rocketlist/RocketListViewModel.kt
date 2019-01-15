package exp.kot.spacex.ui.rocketlist

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import exp.kot.spacex.model.Rocket
import exp.kot.spacex.network.SpaceXRemoteDataSource

/**
 * Created by Luis Silva on 03/10/2018.
 */

class RocketListViewModel : ViewModel() {

    private val spaceXRemoteDataSource: SpaceXRemoteDataSource = SpaceXRemoteDataSource()

    val rocketsResult = MutableLiveData<List<Rocket>>()
    val rocketsError = MutableLiveData<Error>()


    init {
        loadRocketsFromDataSrouce()
    }

    fun loadRocketsFromDataSrouce() {
        spaceXRemoteDataSource.getAllRockets(
            //for this use case only last received values matter (postValue)
            {
                rocketsResult.postValue(it)

                //viewmodel dispatches events only when screen is visible
                //in this scenario
                //if we get valid data we use it in favor of error
                //instead of view, handling observing status following its lifecycle (add/remove as observer)
                //(ie view only observes error when it is visible to user (example to show a dialog))
                //we send null and thus let view know error does not matter, should not be used
                //(obviously this must be known in view side)
                //or it matters to only confirm error states should be hidden (we now have data to show)
                //example: (while view is in background lets say we somehow get) error -> data -> error (same data type)
                //when user is back to screen everything would be dispatched thus errors and data
                //since it was in the background errors do not matter since we have data
                //since valid error uses setValue, postValue will override it
                //other patterns could have been used like for instance a single event class to handle this, but this approach seems
                //pretty simple and clean and we can decide what to do for each data type or request type
                rocketsError.postValue(null)
            },
            {
                rocketsError.value = it

            }
        )
    }


    override fun onCleared() {
        super.onCleared()
        spaceXRemoteDataSource.cancelRequests()

    }


}
