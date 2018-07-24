package github.ll.emoticon.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import github.ll.emoticon.R
import github.ll.emotionboard.data.Emoticon
import github.ll.emotionboard.interfaces.OnEmoticonClickListener
import github.ll.emotionboard.interfaces.GridAdapter
import github.ll.emotionboard.utils.imageloader.ImageLoader

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/7/17
 */
class AppAdapter<T: Emoticon>(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?)
    : GridAdapter<T, AppAdapter.ViewHolder>(context, emoticons, clickListener) {

    override fun createViewHolder(convertView: View): ViewHolder {
        val viewHolder = ViewHolder()

        viewHolder.rootView = convertView.findViewById(R.id.ly_root)
        viewHolder.imageView = convertView.findViewById(R.id.iv_icon) as ImageView
        viewHolder.textView = convertView.findViewById(R.id.tv_name) as TextView

        return viewHolder
    }

    override fun createConvertView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.item_app, null)
    }

    override fun bindView(position: Int, parent: ViewGroup, viewHolder: ViewHolder) {
        val emojiBean = getItem(position) as T

        val uri = emojiBean.uri

        val image = viewHolder.imageView

        if (image != null && uri != null) {
            ImageLoader.displayImage(uri, image)
        }

        viewHolder.rootView?.setOnClickListener({
            clickListener?.onEmoticonClick(emojiBean)
        })
    }

    override fun setViewHolderLayoutParams(viewHolder: ViewHolder, parent: ViewGroup) {
       // do nothing
    }


    class ViewHolder {
        var rootView: View? = null
        var imageView: ImageView? = null
        var textView: TextView? = null
    }
}

