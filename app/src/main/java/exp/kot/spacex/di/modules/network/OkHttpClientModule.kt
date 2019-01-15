package exp.kot.spacex.di.modules.network

import android.app.Application
import dagger.Module
import dagger.Provides
import exp.kot.spacex.BuildConfig
import exp.kot.spacex.helpers.hpHasNetwork
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


/**
 * Created by Luis Silva on 05/10/2018.
 */

@Module
class OkHttpClientModule {

    companion object {
        const val WITH_CACHING = "WITH_CACHING"
        const val WITHOUT_CACHING = "WITHOUT_CACHING"
    }


    @Provides
    @Singleton
    @Named(WITH_CACHING)
    fun okHttpClientCaching(
        cache: Cache,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        app: Application
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .cache(cache)
            .addInterceptor { chain -> chain.proceed(addCache(app, chain)) }
            .also {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(httpLoggingInterceptor)
                }
            }
            .build()
    }


    @Provides
    @Singleton
    @Named(WITHOUT_CACHING)
    fun okHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .also {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(httpLoggingInterceptor)
                }
            }
            .build()
    }


    private fun addCache(app: Application, chain: Interceptor.Chain): Request {
        var request = chain.request()

        request = if (hpHasNetwork(app)) {
            /*
                If there is Internet, get the cache that was stored 1 minute ago.
                If the cache is older than 1 minute, then discard it, and allow error in fetching the response.
                The 'max-age' attribute is responsible for this behavior.
             */
            request.newBuilder().header(
                "Cache-Control",
                "public, max-age=" + TimeUnit.MINUTES.toSeconds(1)
            ).build()
        } else {
            /*
                If there is no Internet, get the cache that was stored 3 days ago.
                If the cache is older than 3 days, then discard it, and allow error in fetching the response.
                The 'max-stale' attribute is responsible for this behavior.
                The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
            */
            request.newBuilder().header(
                "Cache-Control",
                "public, only-if-cached, max-stale=" + TimeUnit.DAYS.toSeconds(3)
            ).build()
        }
        return request
    }


    @Provides
    @Singleton
    fun cache(cacheFile: File): Cache {
        return Cache(cacheFile, 20 * 1024 * 1024) //20 MB
    }

    @Provides
    @Singleton
    fun file(app: Application): File {
        val file = File(app.cacheDir, "HttpCache")
        file.mkdirs()
        return file
    }

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Timber.d(message) })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

}