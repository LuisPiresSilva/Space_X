package exp.kot.spacex.di.modules.network

import dagger.Module
import dagger.Provides
import exp.kot.spacex.di.NetworkWrapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named

/**
 * Created by Luis Silva on 05/10/2018.
 */
@Module(includes = [RetrofitModule::class])
class NetworkModule {


    @Provides
    @Named(OkHttpClientModule.WITH_CACHING)
    fun networkCached(@Named(OkHttpClientModule.WITH_CACHING) builder: Retrofit.Builder, @Named(OkHttpClientModule.WITH_CACHING) okhttpclient: OkHttpClient): NetworkWrapper {
        return NetworkWrapper(builder, okhttpclient)
    }

    @Provides
    @Named(OkHttpClientModule.WITHOUT_CACHING)
    fun networkNonCached(@Named(OkHttpClientModule.WITHOUT_CACHING) builder: Retrofit.Builder, @Named(OkHttpClientModule.WITHOUT_CACHING) okhttpclient: OkHttpClient): NetworkWrapper {
        return NetworkWrapper(builder, okhttpclient)
    }

}