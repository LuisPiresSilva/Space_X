package exp.kot.spacex.ui.rocketdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import exp.kot.spacex.R
import exp.kot.spacex.components.recyclerview.HeaderItemDecoration
import exp.kot.spacex.helpers.hpHasNetwork
import exp.kot.spacex.model.Rocket
import exp.kot.spacex.ui.rocketdetail.recyclerview.RocketDetailRecyclerViewAdapter
import kotlinx.android.synthetic.main.rocketdetail_fragment.*

/**
 * Created by Luis Silva on 03/10/2018.
 */

class RocketDetailFragment : Fragment() {

    companion object {
        fun newInstance(args: Bundle): RocketDetailFragment {
            val rocketDetailFragment = RocketDetailFragment()
            rocketDetailFragment.arguments = args
            return rocketDetailFragment
        }
    }

    private lateinit var rocket: Rocket
    private lateinit var viewModel: RocketDetailViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: LinearLayoutManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.rocketdetail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments != null) {
            rocket = arguments!!.getParcelable(Rocket.TAG_PARCELABLE_ROCKET)
        }

        viewManager = LinearLayoutManager(requireContext())
        viewAdapter = RocketDetailRecyclerViewAdapter(
            requireContext(),
            ArrayList(),
            rocket
        )



        recyclerView = rocketdetail_recyclerview.apply {
            setHasFixedSize(false)

            //just to make animations more visible
            itemAnimator.addDuration = 800
            itemAnimator.removeDuration = 800
            itemAnimator.changeDuration = 800

            layoutManager = viewManager
            adapter = viewAdapter

            isNestedScrollingEnabled = false


        }
        recyclerView.addItemDecoration(
            HeaderItemDecoration(
                recyclerView,
                viewAdapter as RocketDetailRecyclerViewAdapter
            )
        )

        viewModel = ViewModelProviders.of(
            this,
            RocketDetailViewModel.ViewModelFactory(rocket.rocketID)
        ).get(RocketDetailViewModel::class.java)

        viewModel.rocketLaunchesResult.observe(this, Observer {
            rocketdetail_swiperefresh.isRefreshing = false
            (viewAdapter as RocketDetailRecyclerViewAdapter).setList(it!!)
        })

        viewModel.rocketLaunchessError.observe(this, Observer { error ->
            rocketdetail_swiperefresh.isRefreshing = false
            error?.let {
                MaterialDialog.Builder(requireContext())
                    .title(getString(R.string.dialog_error_title))
                    .content(getString(R.string.dialog_error_fetching_content))
                    .positiveText(getString(R.string.dialog_error_positive_action_close))
                    .show()
            }
        })

        rocketdetail_swiperefresh.isRefreshing = true
        rocketdetail_swiperefresh.setOnRefreshListener {
            if (hpHasNetwork(requireContext())) {
                viewModel.loadLaunchesFromDataSrouce(rocket.rocketID)
            } else {
                rocketdetail_swiperefresh.isRefreshing = false
                MaterialDialog.Builder(requireContext())
                    .title(getString(R.string.dialog_error_title))
                    .content(getString(R.string.dialog_error_no_internet_content))
                    .positiveText(getString(R.string.dialog_error_positive_action_close))
                    .show()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.adapter = null
    }
}
