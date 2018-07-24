package github.ll.emotionboard.utils

import android.content.Context

/**
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/6/11
 */

fun Context.getResourceUri(drawableID: Int) = "android.resource://${this.packageName}/$drawableID"


enum class UriType {
    ASSETS,
    DRAWABLE,
    FILE,
    OTHER;
}

object UriUtils {
    fun getUriType(uri: String): UriType {
        if (uri.startsWith("android.resource:")) {
            return UriType.DRAWABLE
        }

        if (uri.startsWith("file:///android_asset/")) {
            return UriType.ASSETS
        }

        if (uri.startsWith("file://") && !uri.contains("android_asset")) {
            return UriType.FILE
        }

        return UriType.OTHER
    }

    fun getResourceID(context: Context, uri: String) = uri.substring("android.resource://${context.packageName}/".length)

    fun getFilePath(uri: String): String? {
        return if (getUriType(uri) != UriType.FILE) {
            null
        } else {
            uri.substring("file://".length)
        }
    }

    fun getAssetsPath(uri: String): String? {
        return if (getUriType(uri) != UriType.ASSETS) {
            null
        } else {
            uri.substring("file:///android_asset/".length)
        }
    }
}

