package github.ll.emoticon.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import github.ll.emoticon.R
import github.ll.emoticon.common.AdapterUtils
import github.ll.emoticon.common.SimpleCommonUtils
import github.ll.emoticon.common.widget.SimpleAppsGridView
import github.ll.emotionboard.adpater.EmoticonPacksAdapter
import github.ll.emotionboard.data.Emoticon
import github.ll.emotionboard.data.EmoticonPack
import github.ll.emotionboard.utils.getResourceUri
import github.ll.emotionboard.widget.FuncLayout

class PackUpdateChatActivity : SimpleChatActivity(), FuncLayout.FuncKeyBoardListener {

    private val addId = 1
    private val deleteId = 2
    private val addPackId = 3
    private val deletePackId = 4

    private var adapter: EmoticonPacksAdapter? = null
    private lateinit var mEmojiPack: EmoticonPack<Emoticon>
    private var packs = mutableListOf<EmoticonPack<out Emoticon>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(1, addId, 1, "add")
        menu.add(1, deleteId, 2, "delete")
        menu.add(1, addPackId, 3, "add pack")
        menu.add(1, deletePackId, 4, "delete pack")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            addId -> {
                val emoticon = Emoticon()
                emoticon.code = "new emoji"
                emoticon.uri = getResourceUri(R.mipmap.icon_emoji)

                mEmojiPack.emoticons.add(emoticon)
                mEmojiPack.isDataChanged = true
                adapter?.notifyDataSetChanged()
            }

            deleteId -> {
                if (!mEmojiPack.emoticons.isEmpty()) {
                    mEmojiPack.emoticons.removeAt(mEmojiPack.emoticons.size - 1)
                    mEmojiPack.isDataChanged = true
                    adapter?.notifyDataSetChanged()
                }
            }

            addPackId -> {
                packs.add(AdapterUtils.getEmoji(this))
                adapter?.notifyDataSetChanged()
            }

            deletePackId -> {
                if (!packs.isEmpty()) {
                    packs.removeAt(packs.size - 1)
                    adapter?.notifyDataSetChanged()
                }
            }

            else -> {
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun initEmoticonsKeyBoardBar() {
        SimpleCommonUtils.initEmoticonsEditText(ekBar!!.etChat)

        mEmojiPack = AdapterUtils.getEmoji(this)

        packs.add(mEmojiPack)
        packs.add(AdapterUtils.getXhsPageSetEntity(this))
    //    packs.add(AdapterUtils.getGoodGoodStudyPageSetEntity(this))
    //    packs.add(AdapterUtils.getKaomojiPageSetEntity(this))

        adapter = EmoticonPacksAdapter(packs)
        adapter?.clickListener = onEmoticonClickListener


        ekBar?.setAdapter(adapter)
        ekBar?.addOnFuncKeyBoardListener(this)
        ekBar?.addFuncView(SimpleAppsGridView(this))

        ekBar?.etChat?.setOnSizeChangedListener { _, _, _, _ -> scrollToBottom() }
        ekBar?.btnSend?.setOnClickListener {
            sendBtnClick(ekBar!!.etChat.text.toString())
            ekBar?.etChat?.setText("")
        }


        val width = resources.getDimension(github.ll.emotionboard.R.dimen.bar_tool_btn_width).toInt()

        val leftView = LayoutInflater.from(this).inflate(github.ll.emotionboard.R.layout.left_toolbtn, null)
        var iv_icon = leftView.findViewById(github.ll.emotionboard.R.id.iv_icon) as ImageView
        val imgParams = LinearLayout.LayoutParams(width, RelativeLayout.LayoutParams.MATCH_PARENT)
        iv_icon.layoutParams = imgParams
        iv_icon.setImageResource(R.mipmap.icon_add)
        leftView.setOnClickListener { Toast.makeText(this@PackUpdateChatActivity, "ADD", Toast.LENGTH_SHORT).show() }

        ekBar?.emoticonsToolBarView?.addFixedToolItemView(leftView, false)

        val rightView = LayoutInflater.from(this).inflate(github.ll.emotionboard.R.layout.right_toolbtn, null)
        iv_icon = rightView.findViewById(github.ll.emotionboard.R.id.iv_icon) as ImageView
        iv_icon.setImageResource(R.mipmap.icon_setting)
        iv_icon.layoutParams = imgParams
        rightView.setOnClickListener { Toast.makeText(this@PackUpdateChatActivity, "SETTING", Toast.LENGTH_SHORT).show() }

        ekBar?.emoticonsToolBarView?.addFixedToolItemView(rightView, true)
    }
}
