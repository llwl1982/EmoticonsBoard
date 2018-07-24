package github.ll.emoticon.qq

import android.content.Context
import android.view.View
import android.widget.BaseAdapter
import github.ll.emoticon.R
import github.ll.emoticon.common.adapter.AppAdapter
import github.ll.emoticon.common.adapter.emoticonadapter.DeleteBtnPageFactory
import github.ll.emotionboard.adpater.EmoticonPacksAdapter
import github.ll.emotionboard.data.Emoticon
import github.ll.emotionboard.data.EmoticonPack
import github.ll.emotionboard.interfaces.OnEmoticonClickListener
import github.ll.emotionboard.interfaces.GridPageFactory
import github.ll.emotionboard.utils.getResourceUri
import sj.qqkeyboard.DefQqEmoticons.sQqEmoticonHashMap

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/7/16
 */
object QQAdapterUtils {

    fun getCommonAdapter(context: Context, emoticonClickListener: OnEmoticonClickListener<Emoticon>?): EmoticonPacksAdapter {
        val packs = mutableListOf<EmoticonPack<out Emoticon>>()

        packs.add(getQQEmoji(context))
        packs.add(getHeartEmoji(context))

        val adapter = EmoticonPacksAdapter(packs)
        adapter.clickListener = emoticonClickListener

        return adapter
    }


    private fun getQQEmoji(context: Context): EmoticonPack<Emoticon> {
        val emojiArray = mutableListOf<Emoticon>()

        sQqEmoticonHashMap.mapTo(emojiArray) {
            val emoticon = Emoticon()
            emoticon.code = it.key
            emoticon.uri = context.getResourceUri(it.value)
            return@mapTo emoticon
        }


        val pack = EmoticonPack<Emoticon>()
        pack.emoticons = emojiArray

        pack.iconUri = context.getResourceUri(R.mipmap.icon_emoji)

        val factory = object: DeleteBtnPageFactory<Emoticon>() {
            override fun <T : Emoticon> createAdapter(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?): BaseAdapter {
                val adapter = super.createAdapter(context, emoticons, clickListener) as DeleteBtnAdapter<T>
                adapter.itemHeightMaxRatio = 1.8

                return adapter
            }
        }

        factory.deleteIconUri = context.getResourceUri(R.mipmap.kys)
        factory.line = 3
        factory.row = 7

        pack.pageFactory = factory

        return pack
    }

    private fun getHeartEmoji(context: Context): EmoticonPack<Emoticon> {
        val emojiArray = mutableListOf<Emoticon>()

        emojiArray.add(Emoticon("QQ电话", context.getResourceUri(R.mipmap.lcw)))
        emojiArray.add(Emoticon("视频电话", context.getResourceUri(R.mipmap.lde)))
        emojiArray.add(Emoticon("短视频", context.getResourceUri(R.mipmap.ldh)))
        emojiArray.add(Emoticon("收藏", context.getResourceUri(R.mipmap.lcx)))
        emojiArray.add(Emoticon("发红包", context.getResourceUri(R.mipmap.ldc)))
        emojiArray.add(Emoticon("转账", context.getResourceUri(R.mipmap.ldk)))
        emojiArray.add(Emoticon("悄悄话", context.getResourceUri(R.mipmap.ldf)))
        emojiArray.add(Emoticon("文件", context.getResourceUri(R.mipmap.lcu)))

        val pack = EmoticonPack<Emoticon>()
        pack.emoticons = emojiArray

        pack.iconUri = context.getResourceUri(R.mipmap.dec)

        val factory = object: GridPageFactory<Emoticon>() {
            override fun create(context: Context, emoticons: List<Emoticon>, clickListener: OnEmoticonClickListener<Emoticon>?): View {
                val pageView = QqGridView(context)

                try {
                    val adapter = createAdapter(context, emoticons, clickListener)
                    pageView.gridView.adapter = adapter
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return pageView
            }

            override fun <T : Emoticon> createAdapter(context: Context, emoticons: List<T>, clickListener: OnEmoticonClickListener<Emoticon>?): BaseAdapter {
                return AppAdapter(context, emoticons, clickListener)
            }
        }

        pack.pageFactory = factory

        return pack
    }
}