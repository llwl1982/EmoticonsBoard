package github.ll.emotionboard.adpater

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import github.ll.emotionboard.data.Emoticon
import github.ll.emotionboard.data.EmoticonPack
import github.ll.emotionboard.interfaces.OnEmoticonClickListener

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/6/7
 */
open class EmoticonPacksAdapter(val packList: List<EmoticonPack<out Emoticon>>): PagerAdapter() {

    var clickListener: OnEmoticonClickListener<Emoticon>? = null
    var adapterListener: EmoticonPacksAdapterListener? = null
    private var oldCount = -1
    private var isUpdateAll = false

    fun getEmoticonPackPosition(pack: EmoticonPack<out Emoticon>): Int {
        var startPosition = 0

        for (it in packList) {
            if (it != pack) {
                startPosition += it.pageCount
            } else {
                break
            }
        }

        return startPosition
    }

    override fun getCount(): Int {
        var count = 0

        packList.forEach {
            count += it.pageCount
        }

        if (oldCount != -1) {
            isUpdateAll = oldCount != count
        }

        oldCount = count

        return count
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any? {
        var pageNumber = position
        var pack: EmoticonPack<*>? = null

        for (emoticonPack in packList) {
            if (emoticonPack.pageCount > pageNumber) {
                pack = emoticonPack
                break
            } else {
                pageNumber -= emoticonPack.pageCount
            }
        }

        val view = pack?.getView(container.context, pageNumber, clickListener)
        view?.tag = position
        container.addView(view)

        pack?.isDataChanged = false

        isUpdateAll = false

        return view
    }

    private fun getEmotionPack(position: Int) : EmoticonPack<*>? {
        var pageNumber = position

        for (emoticonPack in packList) {
            if (emoticonPack.pageCount > pageNumber) {
                return emoticonPack
            } else {
                pageNumber -= emoticonPack.pageCount
            }
        }

        return null
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }


    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }


    override fun getItemPosition(obj: Any?): Int {

        if (isUpdateAll) {
            return POSITION_NONE
        }

        if (packList.isEmpty()) {
            return POSITION_NONE
        }

        if (obj is View) {
            val pack = getEmotionPack(obj.tag as Int)

            if (pack != null && pack.isDataChanged) {
                return POSITION_NONE
            }
        }

        return POSITION_UNCHANGED
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()

        adapterListener?.onDataSetChanged()
    }

    interface EmoticonPacksAdapterListener {
        fun onDataSetChanged()
    }
}