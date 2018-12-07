package exp.kot.spacex

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import exp.kot.spacex.di.component.DaggerNetworkComponent
import exp.kot.spacex.di.modules.context.AppModule
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class NetworkDependencyInjectionTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("exp.kot.kotexperiences", appContext.packageName)
    }


    @Test
    fun globalsInitialisedWithApp() {
        assertNotNull(SpaceXApplication.networkComponent())
    }

    @Test
    fun diExpectedInstances() {

        //we add app context
        val netComp1 = DaggerNetworkComponent.builder().appModule(AppModule(SpaceXApplication.app)).build()
        assertNotNull(netComp1)

        val injectedResult1_1 = netComp1.prepareRetrofitCaching()
        val injectedResult1_2 = netComp1.prepareRetrofitCaching()
        assertNotNull(injectedResult1_1)
        assertNotNull(injectedResult1_2)

        //expect network wrapper should be different
        assertNotEquals(injectedResult1_1, injectedResult1_2)

        //expect that singleton annotation avoids creation of new instances
        assertEquals(injectedResult1_1.retrofitBuilder, injectedResult1_2.retrofitBuilder)
        assertEquals(injectedResult1_1.okhttpclient, injectedResult1_2.okhttpclient)

        injectedResult1_1.buildHost("http://somehost.com")
        injectedResult1_2.buildHost("http://somehost.com")

        //retrofit instances should be different
        assertNotEquals(injectedResult1_1.retrofit, injectedResult1_2.retrofit)


        //the latter shows that network wrapper and retrofit create new instances and are different
        //thus should be released just fine
        //besides being different
        //retrofit is built over the same builder
        //and all work is done over the same http client thus requests should be cancelled upon releasing (if necessary)


        //expect different instances between cached and non cached
        val injectedResult2_1 = netComp1.prepareRetrofitCaching()
        val injectedResult2_2 = netComp1.prepareRetrofitNonCaching()
        val injectedResult2_3 = netComp1.prepareRetrofitNonCaching()
        assertNotNull(injectedResult2_1)
        assertNotNull(injectedResult2_2)
        assertNotNull(injectedResult2_3)

        //expect network wrapper should be different
        assertNotEquals(injectedResult2_1, injectedResult2_2)
        assertNotEquals(injectedResult2_1, injectedResult2_3)

        //expect Named annotation results in different instances
        assertNotEquals(injectedResult2_1.retrofitBuilder, injectedResult2_2.retrofitBuilder)
        assertNotEquals(injectedResult2_1.okhttpclient, injectedResult2_2.okhttpclient)

        //expect that singleton annotation avoids creation of new instances
        assertEquals(injectedResult2_2.retrofitBuilder, injectedResult2_3.retrofitBuilder)
        assertEquals(injectedResult2_2.okhttpclient, injectedResult2_3.okhttpclient)

    }

}
