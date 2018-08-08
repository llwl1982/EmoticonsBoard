package github.ll.emotionboard.interfaces

import github.ll.emotionboard.data.EmoticonPack

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/7/5
 */
interface EmoticonsIndicator {
    /**
     * 移动到一个表情包
     * @param position 表情包中的第几页
     * @param pack 将要移动到的表情包
     */
    fun playTo(position: Int, pack: EmoticonPack<*>)

    fun notifyDataChanged()
}
