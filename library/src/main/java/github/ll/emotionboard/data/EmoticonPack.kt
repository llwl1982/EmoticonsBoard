package github.ll.emotionboard.data

import android.content.Context
import android.view.View
import github.ll.emotionboard.interfaces.OnEmoticonClickListener
import github.ll.emotionboard.interfaces.GridPageFactory
import github.ll.emotionboard.interfaces.PageFactory

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/6/7
 */
class EmoticonPack<T: Emoticon> {
    var iconUri: String? = null
    var name: String? = null
    lateinit var emoticons: MutableList<T>
    var pageFactory: PageFactory<T> = GridPageFactory()
    var isDataChanged = false
    var tag: Any? = null

    val pageCount: Int
    get() {
        if (emoticons == null || pageFactory == null) {
            throw RuntimeException("must set emoticons and pageFactory first")
        }

        var count = emoticons.size / pageFactory.emoticonsCapacity()

        if (emoticons.size % pageFactory.emoticonsCapacity() > 0) {
            count++
        }

        return count
    }


    fun getView(context: Context, pageIndex: Int, listener: OnEmoticonClickListener<Emoticon>?): View {
        return pageFactory.create(context, getEmoticons(pageIndex), listener)
    }

    private fun getEmoticons(pageIndex: Int): List<T> {

        val fromIndex = pageIndex * pageFactory.emoticonsCapacity()
        var toIndex = Math.min((pageIndex + 1) * pageFactory.emoticonsCapacity(), emoticons.size)

        return emoticons.subList(fromIndex, toIndex)
    }
}