package github.ll.emoticon.common;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.sj.emoji.EmojiDisplay;

import github.ll.emoticon.common.filter.EmojiFilter;
import github.ll.emoticon.common.filter.XhsFilter;
import github.ll.emotionboard.utils.EmoticonsKeyboardUtils;
import github.ll.emotionboard.widget.EmoticonsEditText;

public class SimpleCommonUtils {

    public static void initEmoticonsEditText(EmoticonsEditText etContent) {
        etContent.addEmoticonFilter(new EmojiFilter());
        etContent.addEmoticonFilter(new XhsFilter());
    }

    public static void delClick(EditText editText) {
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
    }

    public static void spannableEmoticonFilter(TextView tv_content, String content) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);

        Spannable spannable = EmojiDisplay.spannableFilter(tv_content.getContext(),
                spannableStringBuilder,
                content,
                EmoticonsKeyboardUtils.getFontHeight(tv_content));

        spannable = XhsFilter.spannableFilter(tv_content.getContext(),
                spannable,
                content,
                EmoticonsKeyboardUtils.getFontHeight(tv_content),
                null);
        tv_content.setText(spannable);
    }
}
