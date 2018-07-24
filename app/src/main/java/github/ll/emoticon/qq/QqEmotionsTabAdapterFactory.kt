package github.ll.emoticon.qq

import android.view.LayoutInflater
import android.view.ViewGroup
import github.ll.emoticon.R
import github.ll.emotionboard.data.Emoticon
import github.ll.emotionboard.data.EmoticonPack
import github.ll.emotionboard.widget.EmotionPackTabAdapter
import github.ll.emotionboard.widget.EmotionsTabBar

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/7/17
 */
class QqEmotionsTabAdapterFactory: EmotionsTabBar.DefaultAdapterFactory() {

    override fun getAdapter(packs: List<EmoticonPack<out Emoticon>>): EmotionPackTabAdapter {
        return QqEmotionsPackAdapter(packs)
    }
}

class QqEmotionsPackAdapter(packs: List<EmoticonPack<out Emoticon>>) : EmotionPackTabAdapter(packs) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_toolbtn_qq, parent, false)

        return ViewHolder(view)
    }
}