package exp.kot.spacex

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import exp.kot.spacex.database.SystemDatabase
import exp.kot.spacex.model.SystemUser
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SystemDatabaseTest {

    private lateinit var database: SystemDatabase
    private lateinit var systemUser: SystemUser

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, SystemDatabase::class.java)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val sysUser = SystemUser()
                    db.execSQL("INSERT INTO SystemUser (id, first_time) VALUES ('${sysUser.id}', '${sysUser.firstTime}')")
                }
            })
            .allowMainThreadQueries()
            .build()

        systemUser = database.systemUserDao().getUserSystemSync()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun expectCreateDatabase() {
        assertNotNull(database)
    }


    //SYSTEM USER
    @Test
    fun expectCreatedGlobalSystemUser() {
        assertNotNull(systemUser)
        assertEquals(SystemUser.globalID, systemUser.id)
    }


    @Test
    fun expectOnlyOneAndSameGlobalSystemUser() {
        //here we start with one system user already in the database
        val systemUser = SystemUser()
        systemUser.id = 3 //SystemUser always sets id with Global ID

        database.systemUserDao().insertSync(systemUser)

        //expect only one instance
        assertEquals(1, database.systemUserDao().getAllUserSystemSync().size)

        //expect always the same instance
        assertEquals(SystemUser.globalID, database.systemUserDao().getUserSystemSync().id)

    }

    @Test
    fun expectAlwaysOneGlobalSystemUser() {
        database.systemUserDao().deleteSync(systemUser)

        //expect only one instance
        assertEquals(1, database.systemUserDao().getAllUserSystemSync().size)

        //expect always the same instance
        assertEquals(SystemUser.globalID, database.systemUserDao().getUserSystemSync().id)

    }

    @Test
    fun expectNormalUpdatesSystemUser() {
        val systemUser = database.systemUserDao().getUserSystemSync()
        val first = systemUser.firstTime

        systemUser.firstTime = !first

        database.systemUserDao().updateSync(systemUser)

        //expect updated database object
        assertNotEquals(first, database.systemUserDao().getUserSystemSync().firstTime)
    }


}
