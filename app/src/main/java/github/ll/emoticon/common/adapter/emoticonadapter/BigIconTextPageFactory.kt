package github.ll.emoticon.common.adapter.emoticonadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import github.ll.emoticon.R
import github.ll.emotionboard.data.Emoticon
import github.ll.emotionboard.interfaces.OnEmoticonClickListener
import github.ll.emotionboard.interfaces.GridAdapter
import github.ll.emotionboard.interfaces.GridPageFactory
import github.ll.emotionboard.utils.imageloader.ImageLoader

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/7/11
 */
open class BigIconTextPageFactory<T: Emoticon>: GridPageFactory<T>() {

    override fun <T : Emoticon> createAdapter(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?): BaseAdapter {
        val adapter = BigIconTextEmoticonAdapter(context, emoticons, clickListener)
        adapter.line = line

        return adapter
    }
}

class BigIconTextEmoticonAdapter<T: Emoticon>(context: Context,
                                              emoticons: List<T>,
                                              clickListener: OnEmoticonClickListener<Emoticon>?)
    : GridAdapter<T, BigIconTextEmoticonAdapter.ViewHolder>(context, emoticons, clickListener) {

    internal val DEF_HEIGHT_MAX_RATIO = 1.6

    protected val defaultItemHeight = context.resources.getDimension(R.dimen.item_emoticon_size_big).toInt()
    protected var itemHeight = defaultItemHeight
    protected var itemHeightMax = 0
    protected var itemHeightMin = 0
    protected var itemHeightMaxRatio = DEF_HEIGHT_MAX_RATIO
    var line = 0

    override fun createViewHolder(convertView: View): ViewHolder {
        val viewHolder = ViewHolder()
        viewHolder.rootView = convertView
        viewHolder.rootLayout = convertView.findViewById(R.id.ly_root) as LinearLayout
        viewHolder.imageView = convertView.findViewById(R.id.iv_emoticon) as ImageView
        viewHolder.code = convertView.findViewById(R.id.tv_content) as TextView

        return viewHolder
    }

    override fun createConvertView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.item_emoticon_big, null)
    }

    override fun bindView(position: Int, parent: ViewGroup, viewHolder: ViewHolder) {

        val emojiBean= getItem(position) as T

        viewHolder.rootLayout?.setBackgroundResource(github.ll.emotionboard.R.drawable.bg_emoticon)

        val uri = emojiBean.uri

        val image = viewHolder.imageView

        if (image != null && uri != null) {
            ImageLoader.displayImage(uri, image)
        }

        viewHolder.code?.text = emojiBean.code

        viewHolder.rootView?.setOnClickListener({
            clickListener?.onEmoticonClick(emojiBean)
        })
    }

    override fun setViewHolderLayoutParams(viewHolder: ViewHolder, parent: ViewGroup) {
        if (defaultItemHeight != itemHeight) {
            viewHolder.imageView?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHeight)
        }
        itemHeightMax = if (itemHeightMax!= 0) itemHeightMax else (itemHeight * itemHeightMaxRatio).toInt()
        itemHeightMin = if (this.itemHeightMin != 0) this.itemHeightMin else itemHeight

        var realItemHeight = (parent.parent as View).measuredHeight / line
        realItemHeight = Math.min(realItemHeight, itemHeightMax)
        realItemHeight = Math.max(realItemHeight, itemHeightMin)

        viewHolder.rootLayout?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, realItemHeight)
    }

    class ViewHolder {
        var rootView: View? = null
        var rootLayout: LinearLayout? = null
        var imageView: ImageView? = null
        var code: TextView? = null
    }
}