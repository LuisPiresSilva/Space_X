package exp.kot.spacex.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import exp.kot.spacex.di.NetworkWrapper
import exp.kot.spacex.di.modules.network.NetworkModule
import exp.kot.spacex.di.modules.network.OkHttpClientModule
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Luis Silva on 05/10/2018.
 */

@Singleton
@Component(modules = [NetworkModule::class])
interface NetworkComponent {

    @Named(OkHttpClientModule.WITH_CACHING)
    fun prepareRetrofitCaching(): NetworkWrapper

    @Named(OkHttpClientModule.WITHOUT_CACHING)
    fun prepareRetrofitNonCaching(): NetworkWrapper


//      repeated code but is preferable to writting a new module for context plus hides modules from builder
//      docs: @BindsInstance methods should be preferred to writing a @Module with constructor arguments
//      and immediately providing those values.
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(applicationContext: Application): Builder

        fun build(): NetworkComponent
    }
}