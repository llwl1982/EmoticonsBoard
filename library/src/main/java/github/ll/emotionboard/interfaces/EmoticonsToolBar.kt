package github.ll.emotionboard.interfaces

import android.view.View
import github.ll.emotionboard.data.Emoticon
import github.ll.emotionboard.data.EmoticonPack

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/6/13
 */
interface EmoticonsToolBar {

    fun setToolBarItemClickListener(listener: OnToolBarItemClickListener?)

    fun selectEmotionPack(pack: EmoticonPack<out Emoticon>)

    fun setPackList(packs: List<EmoticonPack<out Emoticon>>)

    fun addFixedToolItemView(view: View?, isRight: Boolean)

    fun notifyDataChanged()
}

interface OnToolBarItemClickListener {
    fun onToolBarItemClick(pack: EmoticonPack<out Emoticon>)
}