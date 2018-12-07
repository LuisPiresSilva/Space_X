package exp.kot.spacex.ui.rocketdetail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import exp.kot.spacex.R
import exp.kot.spacex.helpers.hpInTransaction
import exp.kot.spacex.interfaces.ActivityObligations
import exp.kot.spacex.model.Rocket


/**
 * Created by Luis Silva on 03/10/2018.
 */

class RocketDetailActivity : AppCompatActivity(), ActivityObligations {

    private lateinit var rocket: Rocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rocketdetail_activity)

        rocket = intent.getParcelableExtra(Rocket.TAG_PARCELABLE_ROCKET)

        if (rocket != null) {
            supportActionBar?.title = rocket.name
            if (savedInstanceState == null) {
                supportFragmentManager.hpInTransaction {
                    replace(R.id.rocket_detail_activity_container, RocketDetailFragment.newInstance(intent.extras))
                }
            }

        } else {//something went wrong
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            } else {
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition()
                } else {
                    finish()
                }
            }
        }
        return true
    }


    override fun navigate(caller: Fragment, intent: Intent) {

    }

}