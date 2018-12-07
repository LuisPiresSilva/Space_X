package exp.kot.spacex

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import exp.kot.spacex.database.SystemDatabase
import exp.kot.spacex.helpers.hpInTransaction
import exp.kot.spacex.interfaces.ActivityObligations
import exp.kot.spacex.model.SystemUser
import exp.kot.spacex.ui.rocketdetail.RocketDetailActivity
import exp.kot.spacex.ui.rocketlist.RocketListFragment


/**
 * Created by Luis Silva on 03/10/2018.
 */
class RocketList : AppCompatActivity(), ActivityObligations {

    private lateinit var systemDB: SystemDatabase
    private lateinit var systemUser: LiveData<SystemUser>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rocketlist_activity)

        systemDB = SpaceXApplication.database().openLocalSystemDatabase()


        systemUser = systemDB.systemUserDao().getUserSystem()
        systemUser.observe(this, Observer<SystemUser> { user ->
            if (user != null) {
                if (user.firstTime) {
                    //stop observing before trigger the change (for this use case, not needed anymore)
                    systemUser.removeObservers(this)

                    MaterialDialog.Builder(this)
                        .customView(R.layout.welcome_dialog, false)
                        .positiveText("Close")
                        .show()


                    user.firstTime = false
                    systemDB.updateAsync(user)
                }
            }
        })


        if (savedInstanceState == null) {
            supportFragmentManager.hpInTransaction {
                replace(R.id.container, RocketListFragment.newInstance())
            }
        }

    }


    override fun navigate(caller: Fragment, intent: Intent) {
        //checks here if we navigate or not (example: master detail flow) depending on caller and to where
        when (caller) {
            is RocketListFragment -> {
                intent.setClass(baseContext, RocketDetailActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        systemUser.removeObservers(this)
    }
}
