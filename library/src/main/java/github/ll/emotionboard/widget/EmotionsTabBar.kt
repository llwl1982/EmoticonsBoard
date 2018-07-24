package github.ll.emotionboard.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import github.ll.emotionboard.R
import github.ll.emotionboard.data.Emoticon
import github.ll.emotionboard.data.EmoticonPack
import github.ll.emotionboard.interfaces.EmoticonsToolBar
import github.ll.emotionboard.interfaces.OnToolBarItemClickListener
import github.ll.emotionboard.utils.imageloader.ImageLoader


/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/7/2
 */
open class EmotionsTabBar(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs), EmoticonsToolBar {

    private var recyclerView = RecyclerView(context)
    private var layoutManager: SmoothScrollLayoutManager? = null
    private var emotionPacks: List<EmoticonPack<out Emoticon>>? = null
    private var leftView = FrameLayout(context)
    private var rightView = FrameLayout(context)

    var adapterFactory: EmotionsTabAdapterFactory<out RecyclerView.ViewHolder>? = null

    constructor(context: Context): this(context, null)

    init {
        orientation = LinearLayout.HORIZONTAL

        addFixView(leftView)
        addRecyclerView(context)
        addFixView(rightView)
    }

    private fun addFixView(view: View) {
        val params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 0f)
        params.gravity = Gravity.CENTER
        view.layoutParams = params
        addView(view)
    }

    private fun addRecyclerView(context: Context) {
        recyclerView.layoutParams = LayoutParams(0, WRAP_CONTENT, 1f)
        layoutManager = SmoothScrollLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        addView(recyclerView)

        adapterFactory = DefaultAdapterFactory()
    }

    override fun setToolBarItemClickListener(listener: OnToolBarItemClickListener?) {
        adapterFactory?.itemClickListeners = listener
    }

    override fun selectEmotionPack(pack: EmoticonPack<out Emoticon>) {
        val position = emotionPacks?.indexOf(pack)

        val manager = layoutManager

        if (position != null && position >= 0 && manager != null) {
            val firstPosition = manager.findFirstVisibleItemPosition()
            val lastPosition = manager.findLastVisibleItemPosition()

            if (position < firstPosition) {
                manager.isMoveToTop = true
                recyclerView.smoothScrollToPosition(position)
            } else if (position > lastPosition) {
                manager.isMoveToTop = false
                recyclerView.smoothScrollToPosition(position)
            }
        }

        if (position != null) {
            adapterFactory?.onEmotionPackSelect(position)

            recyclerView.adapter.notifyDataSetChanged()
        }
    }

    override fun setPackList(packs: List<EmoticonPack<out Emoticon>>) {
        emotionPacks = packs

        recyclerView.adapter = adapterFactory?.createAdapter(packs)
    }

    override fun addFixedToolItemView(view: View?, isRight: Boolean) {
        if (view != null) {
            val container = if (isRight) rightView else leftView

            container.addView(view)
        }
    }

    override fun notifyDataChanged() {
        recyclerView.adapter.notifyDataSetChanged()
    }

    interface EmotionsTabAdapterFactory<T: RecyclerView.ViewHolder> {
        var itemClickListeners: OnToolBarItemClickListener?
        fun createAdapter(packs: List<EmoticonPack<out Emoticon>>): RecyclerView.Adapter<out T>

        fun onEmotionPackSelect(position: Int)
    }

    open class  DefaultAdapterFactory: EmotionsTabAdapterFactory<EmotionPackTabAdapter.ViewHolder> {

        override var itemClickListeners: OnToolBarItemClickListener? = null
        private lateinit var packList: List<EmoticonPack<out Emoticon>>

        override fun createAdapter(packs: List<EmoticonPack<out Emoticon>>): RecyclerView.Adapter<out EmotionPackTabAdapter.ViewHolder> {
            packList = packs

            packList.forEach {
                it.tag = false
            }

            val adapter = getAdapter(packList)
            adapter.itemClickListeners = itemClickListeners

            return adapter
        }

        protected open fun getAdapter(packs: List<EmoticonPack<out Emoticon>>): EmotionPackTabAdapter {
            return  EmotionPackTabAdapter(packs)
        }

        override fun onEmotionPackSelect(position: Int) {
            packList.forEachIndexed { index, pair ->
                pair.tag = index == position
            }
        }

    }
}

/**
 * packs: MutablePair first is selected state
 */
open class EmotionPackTabAdapter(val packs: List<EmoticonPack<out Emoticon>>)
    : RecyclerView.Adapter<EmotionPackTabAdapter.ViewHolder>() {

    var itemClickListeners: OnToolBarItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.left_toolbtn, parent, false)

        return ViewHolder(view)
    }


    override fun getItemCount() = packs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val context = holder.icon.context

        ImageLoader.displayImage(packs[position].iconUri!!, holder.icon)

        if (packs[position].tag as Boolean) {
            holder.rootView.setBackgroundColor(context.resources.getColor(R.color.toolbar_btn_select))
        } else {
            holder.rootView.setBackgroundResource(R.drawable.btn_toolbtn_bg)
        }

        holder.rootView.isClickable = true

        holder.rootView.setOnClickListener {
            itemClickListeners?.onToolBarItemClick(packs[position])
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootView = itemView
        var icon = itemView.findViewById(R.id.iv_icon) as ImageView
    }
}



data class MutablePair<A, B>(
        var first: A,
        var second: B)  {


    override fun toString(): String = "($first, $second)"
}
