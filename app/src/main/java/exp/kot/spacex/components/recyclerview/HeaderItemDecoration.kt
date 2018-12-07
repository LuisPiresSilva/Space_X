package exp.kot.spacex.components.recyclerview

import android.graphics.Canvas
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

/**
 * Created by Luis Silva on 05/10/2018.
 * not mine
 * source: https://stackoverflow.com/a/44327350
 * have done some custom changes to fix some problems and improve it
 */
class HeaderItemDecoration(recyclerView: RecyclerView, @param:NonNull private val mListener: StickyHeaderInterface) :
    RecyclerView.ItemDecoration() {
    private var mStickyHeaderHeight: Int = 0

    init {

        // On Sticky Header Click
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent): Boolean {
                return motionEvent.y <= mStickyHeaderHeight
            }

            override fun onTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent) {

            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }
        })
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)

        val topChild = parent.getChildAt(0)
        if (topChild == null) {
            return
        }

        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION || mListener.hideForPosition(topChildPosition)) {
            return
        }

        val currentHeader = getHeaderViewForItem(topChildPosition, parent)
        val contactPoint = currentHeader.bottom
        val childInContact = getChildInContact(parent, contactPoint)
        if (childInContact == null) {
            return
        }

        if (mListener.isHeader(parent.getChildAdapterPosition(childInContact))) {
            moveHeader(c, currentHeader, childInContact)
            return
        }

        drawHeader(c, currentHeader)
    }

    //to avoid constant inflation and binding
    private lateinit var currentHeader: View
    private var lastHeader: Int = 0
    private var lastPos: Int = -1
    private fun getHeaderViewForItem(itemPosition: Int, parent: RecyclerView): View {
        val headerPosition = mListener.getHeaderPositionForItem(itemPosition)
        val layoutResId = mListener.getHeaderLayout(headerPosition)
        if (lastHeader != layoutResId) {
            currentHeader = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
            lastHeader = layoutResId
        }
        if (lastPos != itemPosition) {
            mListener.bindHeaderData(currentHeader, headerPosition)
            lastPos = itemPosition
            fixLayoutSize(parent, currentHeader)
        }
        return currentHeader
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }

    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(
            0f,
            (nextHeader.top - (nextHeader.layoutParams as ViewGroup.MarginLayoutParams).topMargin - currentHeader.height).toFloat()
        )
        currentHeader.draw(c)
        c.restore()
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val childMargins: ViewGroup.MarginLayoutParams = child.layoutParams as ViewGroup.MarginLayoutParams

            // This child overlaps the contactPoint
            if ((child.bottom + (childMargins.bottomMargin)) > contactPoint
                && (child.top - (childMargins.topMargin)) <= contactPoint
            ) {
                childInContact = child
                break
            }
        }
        return childInContact
    }

    /**
     * Properly measures and layouts the top sticky header.
     * @param parent ViewGroup: RecyclerView in this case.
     */
    private fun fixLayoutSize(parent: ViewGroup, view: View) {

        // Specs for parent (RecyclerView)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        // Specs for children (headers)
        val childWidthSpec =
            ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidthSpec, childHeightSpec)
        mStickyHeaderHeight = view.measuredHeight
        view.layout(0, 0, view.measuredWidth, mStickyHeaderHeight)
    }

    interface StickyHeaderInterface {

        /**
         * This method gets called by [HeaderItemDecoration] to fetch the position of the header item in the adapter
         * that is used for (represents) item at specified position.
         * @param itemPosition int. Adapter's position of the item for which to do the search of the position of the header item.
         * @return int. Position of the header item in the adapter.
         */
        fun getHeaderPositionForItem(itemPosition: Int): Int

        /**
         * This method gets called by [HeaderItemDecoration] to get layout resource id for the header item at specified adapter's position.
         * @param headerPosition int. Position of the header item in the adapter.
         * @return int. Layout resource id.
         */
        fun getHeaderLayout(headerPosition: Int): Int

        /**
         * This method gets called by [HeaderItemDecoration] to setup the header View.
         * @param headerView View. Header to set the data on.
         * @param headerPosition int. Position of the header item in the adapter.
         */
        fun bindHeaderData(headerView : View, headerPosition: Int)

        /**
         * This method gets called by [HeaderItemDecoration] to verify whether the item represents a header.
         * @param itemPosition int.
         * @return true, if item at the specified adapter's position represents a header.
         */
        fun isHeader(itemPosition: Int): Boolean

        /**
         * This method gets called by [HeaderItemDecoration] to verify whether we are out of a header section.
         *
         * &#8203;
         *
         * (hide in position 0 and between 7 and 10)&#8203;
         *
         * example:&#8203;
         *
         * return itemPosition == 0 || (itemPosition in 7..10)
         * @param itemPosition int.
         * @return true, if the header should not be shown in some position.
         */
        fun hideForPosition(itemPosition: Int): Boolean
    }
}