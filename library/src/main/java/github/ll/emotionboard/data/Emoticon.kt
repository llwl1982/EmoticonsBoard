package github.ll.emotionboard.data

/**
 * 表情数据
 * @author Liliang
 * Email: 53127822@qq.com
 * @date 2018/6/7
 */
open class Emoticon() {

    constructor(code: String?, uri: String?): this() {
        this.code = code
        this.uri = uri
    }

    var code: String? = null
    var uri: String? = null
}