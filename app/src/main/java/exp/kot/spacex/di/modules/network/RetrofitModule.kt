package exp.kot.spacex.di.modules.network

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Luis Silva on 05/10/2018.
 */
@Module(includes = [OkHttpClientModule::class])
class RetrofitModule {


    @Provides
    @Singleton
    @Named(OkHttpClientModule.WITH_CACHING)
    fun retrofitCachedNewHost(@Named(OkHttpClientModule.WITH_CACHING) okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder().also { build(it, okHttpClient) }
    }

    @Provides
    @Singleton
    @Named(OkHttpClientModule.WITHOUT_CACHING)
    fun retrofitNonCachedNewHost(@Named(OkHttpClientModule.WITHOUT_CACHING) okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder().also { build(it, okHttpClient) }
    }


    private fun build(builder: Retrofit.Builder, okHttpClient: OkHttpClient) {
        builder
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
    }
}