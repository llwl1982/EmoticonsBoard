package github.ll.emoticon.userdef;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import github.ll.emoticon.R;
import github.ll.emotionboard.EmoticonsBoard;
import github.ll.emotionboard.utils.EmoticonsKeyboardUtils;

public class SimpleUserEmoticonsBoard extends EmoticonsBoard {

    public final int APPS_HEIGHT = 120;

    public SimpleUserEmoticonsBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void inflateKeyboardBar(){
        inflater.inflate(R.layout.view_keyboard_userdef, this);
    }

    @Override
    protected View inflateFunc(){
        return inflater.inflate(R.layout.view_func_emoticon_userdef, null);
    }

    @Override
    public void reset() {
        EmoticonsKeyboardUtils.closeSoftKeyboard(getContext());
        funcLayout.hideAllFuncView();
        btnFace.setImageResource(R.mipmap.chatting_emoticons);
    }

    @Override
    public void onFuncChange(int key) {
        if (FUNC_TYPE_EMOTION == key) {
            btnFace.setImageResource(R.mipmap.chatting_softkeyboard);
        } else {
            btnFace.setImageResource(R.mipmap.chatting_emoticons);
        }
        checkVoice();
    }

    @Override
    public void onSoftKeyboardClose() {
        super.onSoftKeyboardClose();
        if (funcLayout.getCurrentFuncKey() == FUNC_TYPE_APPPS) {
            setFuncViewHeight(EmoticonsKeyboardUtils.dip2px(getContext(), APPS_HEIGHT));
        }
    }

    @Override
    protected void showText() {
        emoticonsEditText.setVisibility(VISIBLE);
        btnFace.setVisibility(VISIBLE);
        btnVoice.setVisibility(GONE);
    }

    @Override
    protected void showVoice() {
        emoticonsEditText.setVisibility(GONE);
        btnFace.setVisibility(GONE);
        btnVoice.setVisibility(VISIBLE);
        reset();
    }

    @Override
    protected void checkVoice() {
        if (btnVoice.isShown()) {
            btnVoiceOrText.setImageResource(R.mipmap.chatting_softkeyboard);
        } else {
            btnVoiceOrText.setImageResource(R.mipmap.chatting_vodie);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == github.ll.emotionboard.R.id.btn_voice_or_text) {
            if (emoticonsEditText.isShown()) {
                btnVoiceOrText.setImageResource(R.mipmap.chatting_softkeyboard);
                showVoice();
            } else {
                showText();
                btnVoiceOrText.setImageResource(R.mipmap.chatting_vodie);
                EmoticonsKeyboardUtils.openSoftKeyboard(emoticonsEditText);
            }
        } else if (i == github.ll.emotionboard.R.id.btn_face) {
            toggleFuncView(FUNC_TYPE_EMOTION);
        } else if (i == github.ll.emotionboard.R.id.btn_multimedia) {
            toggleFuncView(FUNC_TYPE_APPPS);
            setFuncViewHeight(EmoticonsKeyboardUtils.dip2px(getContext(), APPS_HEIGHT));
        }
    }
}
