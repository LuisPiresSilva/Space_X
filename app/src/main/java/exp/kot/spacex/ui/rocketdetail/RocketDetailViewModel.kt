package exp.kot.spacex.ui.rocketdetail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import exp.kot.spacex.model.Launch
import exp.kot.spacex.network.SpaceXRemoteDataSource

/**
 * Created by Luis Silva on 04/10/2018.
 */
class RocketDetailViewModel(rocketID: String) : ViewModel() {

    private val spaceXRemoteDataSource: SpaceXRemoteDataSource = SpaceXRemoteDataSource()

    val rocketLaunchesResult = MutableLiveData<List<Launch>>()
    val rocketLaunchessError = MutableLiveData<Error>()

    init {
        loadLaunchesFromDataSrouce(rocketID)
    }

    fun loadLaunchesFromDataSrouce(rocketID: String) {
        spaceXRemoteDataSource.getAllRocketLaunches(rocketID,
            //for this use case only last received values matter (postValue)
            {
                rocketLaunchesResult.postValue(it)

                rocketLaunchessError.postValue(null)
            },
            {
                rocketLaunchessError.value = it
            }
        )
    }


    override fun onCleared() {
        super.onCleared()
        spaceXRemoteDataSource.cancelRequests()

    }

    class ViewModelFactory(private val mParam: String) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RocketDetailViewModel(mParam) as T
        }
    }
}
