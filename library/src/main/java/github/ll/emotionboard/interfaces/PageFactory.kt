package github.ll.emotionboard.interfaces

import android.content.Context
import android.view.View
import github.ll.emotionboard.data.Emoticon

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/6/7
 *
 * 表情页工厂
 */
interface PageFactory<T: Emoticon> {
    /**
     * 每页能显示的表情数
     */
    fun emoticonsCapacity(): Int

    /**
     * 创建用于显示表情的View
     */
    fun create(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?): View
}