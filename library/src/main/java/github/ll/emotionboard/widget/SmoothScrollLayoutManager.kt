package github.ll.emotionboard.widget

import android.content.Context
import android.graphics.PointF
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/7/3
 */
class SmoothScrollLayoutManager(val context: Context, orientation: Int, reverseLayout: Boolean) : LinearLayoutManager(context, orientation, reverseLayout) {

    // millisecondsPerInch
    private var millisecondsPerInch = 100f
    var isMoveToTop = false

    constructor(context: Context) : this(context, VERTICAL, false)


    override fun smoothScrollToPosition(recyclerView: RecyclerView,
                                        state: RecyclerView.State?, position: Int) {

        val smoothScroller = object : LinearSmoothScroller(recyclerView.context) {
            // 返回：滑过1px时经历的时间(ms)。
            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return millisecondsPerInch / displayMetrics.densityDpi
            }

            override fun computeScrollVectorForPosition(targetPosition: Int): PointF {
                return this@SmoothScrollLayoutManager
                        .computeScrollVectorForPosition(targetPosition)
            }

            override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
                return if (isMoveToTop) boxStart-viewStart
                else
                    super.calculateDtToFit(viewStart, viewEnd, boxStart, boxEnd, snapPreference)
            }
        }

        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    fun setScollSpeed(millisecondsPerInch: Float) {
        this.millisecondsPerInch = millisecondsPerInch
    }
}
