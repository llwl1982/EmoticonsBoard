package github.ll.emoticon.qq

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.GridView
import android.widget.RelativeLayout
import github.ll.emoticon.R
import github.ll.emotionboard.utils.EmoticonsKeyboardUtils

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/7/16
 */
class QqGridView(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    var gridView: GridView

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.view_apps, this)
        setBackgroundColor(resources.getColor(R.color.white))

        gridView = view.findViewById(R.id.gv_apps) as GridView
        gridView.setPadding(0, 0, 0, EmoticonsKeyboardUtils.dip2px(context, 20f))
        val params = gridView.layoutParams as RelativeLayout.LayoutParams
        params.bottomMargin = EmoticonsKeyboardUtils.dip2px(context, 20f)
        gridView.layoutParams = params
        gridView.verticalSpacing = EmoticonsKeyboardUtils.dip2px(context, 18f)
    }

    constructor(context: Context): this(context, null)
}