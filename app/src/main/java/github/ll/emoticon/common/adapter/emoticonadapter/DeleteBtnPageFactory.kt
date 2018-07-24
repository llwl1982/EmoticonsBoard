package github.ll.emoticon.common.adapter.emoticonadapter

import android.content.Context
import android.view.ViewGroup
import android.widget.BaseAdapter
import github.ll.emoticon.R
import github.ll.emoticon.common.data.PlaceHoldEmoticon
import github.ll.emotionboard.data.Emoticon
import github.ll.emotionboard.interfaces.OnEmoticonClickListener
import github.ll.emotionboard.interfaces.GridPageFactory
import github.ll.emotionboard.interfaces.ImageAdapter
import github.ll.emotionboard.utils.getResourceUri

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/6/12
 */
open class DeleteBtnPageFactory<T: Emoticon>: GridPageFactory<T>() {

    var deleteBtnStatus = ButtonStatus.LAST
    var deleteIconUri: String? = null

    enum class ButtonStatus {
        FOLLOW,
        LAST;
    }

    override fun emoticonsCapacity(): Int {
        return line * row - 1
    }

    override fun <T : Emoticon> createAdapter(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?): BaseAdapter {
        if (deleteIconUri == null) {
            deleteIconUri = context.getResourceUri(R.mipmap.icon_del)
        }
        val adapter = DeleteBtnAdapter(context, emoticons, clickListener)
        adapter.line = line

        return adapter
    }

    inner class DeleteBtnAdapter<T: Emoticon>(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?)
        : ImageAdapter<T>(context, emoticons, clickListener) {
        override fun getCount(): Int {
            return this@DeleteBtnPageFactory.line * row
        }

        override fun getItem(position: Int): Any {
            fun createDelete(): DeleteEmoticon {
                val delete = DeleteEmoticon()
                delete.uri = deleteIconUri
                return delete
            }

            if (position < emoticons.size) {
                return emoticons[position]
            } else {
                return if (deleteBtnStatus == ButtonStatus.FOLLOW) {
                    if (position == emoticons.size) {
                        createDelete()
                    } else {
                        PlaceHoldEmoticon()
                    }
                } else {
                    if (position == count - 1) {
                        createDelete()
                    } else {
                        PlaceHoldEmoticon()
                    }
                }

            }
        }

        override fun bindView(position: Int, parent: ViewGroup, viewHolder: ViewHolder) {
            super.bindView(position, parent, viewHolder)

            val emojiBean = getItem(position)

            viewHolder.rootLayout?.isClickable = emojiBean is PlaceHoldEmoticon

            if (emojiBean is PlaceHoldEmoticon) {
                viewHolder.rootLayout?.setBackgroundResource(0)
            }
        }
    }
}


class DeleteEmoticon: Emoticon()