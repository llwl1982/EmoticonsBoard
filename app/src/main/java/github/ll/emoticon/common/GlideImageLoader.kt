package github.ll.emoticon.common

import android.widget.ImageView
import com.bumptech.glide.Glide
import github.ll.emotionboard.utils.imageloader.IImageLoader

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/7/18
 */
class GlideImageLoader: IImageLoader {
    override fun displayImage(uri: String, imageView: ImageView) {
        Glide.with(imageView.context)
                .load(uri)
                .into(imageView)
    }
}