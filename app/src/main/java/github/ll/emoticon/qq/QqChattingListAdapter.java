package github.ll.emoticon.qq;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import github.ll.emoticon.common.adapter.ChattingListAdapter;
import github.ll.emoticon.common.filter.QqFilter;
import github.ll.emotionboard.utils.EmoticonsKeyboardUtils;

public class QqChattingListAdapter extends ChattingListAdapter {

    public QqChattingListAdapter(Activity activity) {
        super(activity);
    }

    @Override
    public void setContent(TextView tv_content, String content) {

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(content);
        Spannable spannable = QqFilter.spannableFilter(tv_content.getContext(),
                spannableStringBuilder,
                content,
                EmoticonsKeyboardUtils.getFontHeight(tv_content),
                null);
        tv_content.setText(spannable);
    }
}