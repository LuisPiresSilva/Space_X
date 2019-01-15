package exp.kot.spacex.ui.rocketlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import exp.kot.spacex.R
import exp.kot.spacex.RocketList
import exp.kot.spacex.helpers.hpHasNetwork
import exp.kot.spacex.model.Rocket
import exp.kot.spacex.ui.rocketlist.recyclerview.RocketListRecyclerViewAdapter
import kotlinx.android.synthetic.main.rocketlist_fragment.*
import java.util.*

/**
 * Created by Luis Silva on 03/10/2018.
 */

class RocketListFragment : Fragment(), RocketListRecyclerViewAdapter.AdapterCallBack {

    companion object {
        fun newInstance() = RocketListFragment()
    }

    private lateinit var viewModel: RocketListViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.rocketlist_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewManager = LinearLayoutManager(requireContext())
        viewAdapter = RocketListRecyclerViewAdapter(ArrayList())
            .also {adapter ->
                adapter.sendCallBacksTo(this)
            }

        recyclerView = rocketlist_recyclerview.apply {
            setHasFixedSize(false)

            itemAnimator.addDuration = 800
            itemAnimator.removeDuration = 800
            itemAnimator.changeDuration = 800

            layoutManager = viewManager
            adapter = viewAdapter

        }


        viewModel = ViewModelProviders.of(this).get(RocketListViewModel::class.java)
        viewModel.rocketsResult.observe(this, Observer { rocketList ->
            rocketlist_swiperefresh.isRefreshing = false
            (viewAdapter as RocketListRecyclerViewAdapter).setList(rocketList!!)
        })

        viewModel.rocketsError.observe(this, Observer { error ->
            rocketlist_swiperefresh.isRefreshing = false

            error?.let {
                MaterialDialog.Builder(requireContext())
                    .title(getString(R.string.dialog_error_title))
                    .content(getString(R.string.dialog_error_fetching_content))
                    .positiveText(getString(R.string.dialog_error_positive_action_close))
                    .show()
            }
        })

        rocketlist_swiperefresh.isRefreshing = true
        rocketlist_swiperefresh.setOnRefreshListener {
            if (hpHasNetwork(requireContext())) {
                viewModel.loadRocketsFromDataSrouce()
            } else {
                rocketlist_swiperefresh.isRefreshing = false
                MaterialDialog.Builder(requireContext())
                    .title(getString(R.string.dialog_error_title))
                    .content(getString(R.string.dialog_error_no_internet_content))
                    .positiveText(getString(R.string.dialog_error_positive_action_close))
                    .show()
            }

        }


        rocketlist_active_input.setOnCheckedChangeListener { buttonView, isChecked ->
            (viewAdapter as RocketListRecyclerViewAdapter).filterList(isChecked)
        }


    }

    override fun rocketClick(rocket: Rocket) {
        val intent = Intent()
        intent.putExtra(Rocket.TAG_PARCELABLE_ROCKET, rocket)
        (activity as RocketList).navigate(this, intent)
    }

    override fun rocketLongClick(rocket: Rocket) {

    }


}

