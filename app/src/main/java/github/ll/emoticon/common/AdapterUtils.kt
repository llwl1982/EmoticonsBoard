package github.ll.emoticon.common

import android.content.Context
import android.text.TextUtils
import android.util.Xml
import android.widget.EditText
import com.sj.emoji.DefEmoticons
import com.testemticon.DefXhsEmoticons
import github.ll.emoticon.R
import github.ll.emoticon.common.adapter.emoticonadapter.BigIconTextPageFactory
import github.ll.emoticon.common.adapter.emoticonadapter.DeleteBtnPageFactory
import github.ll.emoticon.common.adapter.emoticonadapter.DeleteEmoticon
import github.ll.emoticon.common.adapter.emoticonadapter.TextPageFactory
import github.ll.emoticon.common.data.BigEmoticon
import github.ll.emoticon.common.data.PlaceHoldEmoticon
import github.ll.emotionboard.adpater.EmoticonPacksAdapter
import github.ll.emotionboard.data.Emoticon
import github.ll.emotionboard.data.EmoticonPack
import github.ll.emotionboard.interfaces.GridPageFactory
import github.ll.emotionboard.interfaces.OnEmoticonClickListener
import github.ll.emotionboard.utils.getResourceUri
import org.xmlpull.v1.XmlPullParser
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/6/8
 */
object AdapterUtils {

    fun getCommonAdapter(context: Context, emoticonClickListener: OnEmoticonClickListener<Emoticon>?): EmoticonPacksAdapter {
        val packs = mutableListOf<EmoticonPack<out Emoticon>>()

        packs.add(getEmoji(context))
        packs.add(getXhsPageSetEntity(context))
        packs.add(getGoodGoodStudyPageSetEntity(context))
        packs.add(getKaomojiPageSetEntity(context))

        val adapter = EmoticonPacksAdapter(packs)
        adapter.clickListener = emoticonClickListener

        return adapter
    }

    fun getEmoji(context: Context): EmoticonPack<Emoticon> {
        val emojiArray = mutableListOf<Emoticon>()

        DefEmoticons.sEmojiArray.take(21).mapTo(emojiArray) {
            val emoticon = Emoticon()
            emoticon.code = it.emoji
            emoticon.uri = context.getResourceUri(it.icon)
            return@mapTo emoticon
        }

        val pack = EmoticonPack<Emoticon>()
        pack.emoticons = emojiArray

        pack.iconUri = context.getResourceUri(R.mipmap.icon_emoji)

        val factory = DeleteBtnPageFactory<Emoticon>()
        factory.deleteIconUri = context.getResourceUri(R.mipmap.icon_del)
        factory.line = 3
        factory.row = 7

        pack.pageFactory = factory

        return pack
    }

    fun getXhsPageSetEntity(context: Context): EmoticonPack<Emoticon> {
        val pack = EmoticonPack<Emoticon>()
        pack.emoticons = parseXhsData(DefXhsEmoticons.xhsEmoticonArray).subList(0, 10)

        pack.iconUri = "file:///android_asset/xhsemoji_19.png"

        val factory = GridPageFactory<Emoticon>()
   //     factory.deleteBtnStatus = DeleteBtnPageFactory.ButtonStatus.FOLLOW
   //     factory.deleteIconUri = context.getResourceUri(R.mipmap.icon_del)
        factory.line = 3
        factory.row = 7

        pack.pageFactory = factory

        return pack
    }

    fun getGoodGoodStudyPageSetEntity(context: Context): EmoticonPack<Emoticon> {

        val xmlName = "goodgoodstudy/goodgoodstudy.xml"
        val inStream = context.getResources().getAssets().open(xmlName)

        return parserXml("file:///android_asset/goodgoodstudy", inStream)
    }

    fun getKaomojiPageSetEntity(context: Context): EmoticonPack<Emoticon> {

        val pack = EmoticonPack<Emoticon>()
        pack.emoticons = parseKaomojiData(context)
        pack.iconUri = context.getResourceUri(R.mipmap.icon_kaomoji)

        val factory = TextPageFactory<Emoticon>()
        factory.line = 3
        factory.row = 3

        pack.pageFactory = factory

        return pack
    }

    private fun parseKaomojiData(context: Context): MutableList<Emoticon> {
        val emojis = mutableListOf<Emoticon>()

        val inputStreamReader = InputStreamReader(context.assets.open("kaomoji"))
        val bufferedReader = BufferedReader(inputStreamReader)
        var line = bufferedReader.readLine()

        while (line != null) {
            val bean = Emoticon()
            bean.code = line.trim { it <= ' ' }

            emojis.add(bean)

            line = bufferedReader.readLine()
        }
        return emojis
    }

    private fun parseXhsData(array: Array<String>): MutableList<Emoticon> {
        val emojis = mutableListOf<Emoticon>()

        array.forEach {
            if (!TextUtils.isEmpty(it)) {
                val temp = it.trim { it <= ' ' }

                val text = temp.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                if (text.size == 2) {
                    val emoticon = Emoticon()

                    emoticon.uri = "file:///android_asset/${text[0]}"
                    emoticon.code = text[1]

                    emojis.add(emoticon)
                }
            }
        }
        return emojis
    }

    private fun parserXml(filePath: String, inStream: InputStream): EmoticonPack<Emoticon> {

        val arrayParentKey = "Emoticon"
        var isChildCheck = false

        val emoticonPack = EmoticonPack<Emoticon>()
        val emoticonList = mutableListOf<Emoticon>()
        emoticonPack.emoticons = emoticonList

        val factory = BigIconTextPageFactory<Emoticon>()
        emoticonPack.pageFactory = factory

        var emoticon: BigEmoticon? = null

        val pullParser = Xml.newPullParser()
        pullParser.setInput(inStream, "UTF-8")
        var event = pullParser.eventType

        while (event != XmlPullParser.END_DOCUMENT) {
            when (event) {

                XmlPullParser.START_DOCUMENT -> {
                }
                XmlPullParser.START_TAG -> {
                    val keyName = pullParser.name

                    /**
                     * Emoticons data
                     */
                    if (isChildCheck && emoticon != null) {
                        when (keyName) {
                            "iconUri" -> {
                                val value = pullParser.nextText()
                                emoticon.uri = "$filePath/$value"
                            }
                            "content" -> {
                                val value = pullParser.nextText()
                                emoticon.code = value
                            }
                        }
                    } else {
                        when (keyName) {
                            "name" -> emoticonPack.name = pullParser.nextText()
                            "line" -> factory.line = pullParser.nextText().toInt()
                            "row" -> factory.row = pullParser.nextText().toInt()
                            "iconUri" -> emoticonPack.iconUri = "$filePath/${pullParser.nextText()}"
                        }
                    }

                    if (keyName == arrayParentKey) {
                        isChildCheck = true
                        emoticon = BigEmoticon()
                    }
                }

                XmlPullParser.END_TAG -> {
                    val keyName = pullParser.name
                    if (isChildCheck && keyName == arrayParentKey) {
                        isChildCheck = false

                        if (emoticon != null) {
                            emoticonList.add(emoticon)
                        }
                    }
                }
            }

            event = pullParser.next()
        }

        return emoticonPack
    }

    fun getCommonEmoticonClickListener(editText: EditText): OnEmoticonClickListener<Emoticon> {
        return CommonEmoticonClickListener(editText)
    }

    class CommonEmoticonClickListener(private val editText: EditText): OnEmoticonClickListener<Emoticon> {
        override fun onEmoticonClick(emoticon: Emoticon?) {
            if (emoticon == null) {
                return
            }

            if (emoticon is DeleteEmoticon) {
                SimpleCommonUtils.delClick(editText)
            } else if (emoticon is PlaceHoldEmoticon) {
                // do nothing
            } else if (TextUtils.isEmpty(emoticon.code))  else {
                val index = editText.selectionStart
                val editable = editText.text
                editable.insert(index, emoticon.code)
            }
        }

    }
}