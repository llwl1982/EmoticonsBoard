package github.ll.emotionboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import github.ll.emotionboard.adpater.EmoticonPacksAdapter;
import github.ll.emotionboard.data.Emoticon;
import github.ll.emotionboard.data.EmoticonPack;
import github.ll.emotionboard.interfaces.EmoticonsIndicator;
import github.ll.emotionboard.interfaces.EmoticonsToolBar;
import github.ll.emotionboard.interfaces.OnToolBarItemClickListener;
import github.ll.emotionboard.utils.EmoticonsKeyboardUtils;
import github.ll.emotionboard.widget.AutoHeightLayout;
import github.ll.emotionboard.widget.EmoticonsEditText;
import github.ll.emotionboard.widget.EmoticonsFuncView;
import github.ll.emotionboard.widget.EmoticonsIndicatorView;
import github.ll.emotionboard.widget.FuncLayout;

public class XEmoticonsKeyBoard extends AutoHeightLayout implements View.OnClickListener, EmoticonsFuncView.EmoticonsFuncListener, OnToolBarItemClickListener, EmoticonsEditText.OnBackKeyClickListener, FuncLayout.OnFuncChangeListener {

    public static final int FUNC_TYPE_EMOTION = -1;
    public static final int FUNC_TYPE_APPPS = -2;

    protected LayoutInflater inflater;
    protected ImageView btnVoiceOrText;
    protected Button btnVoice;
    protected EmoticonsEditText emoticonsEditText;
    protected ImageView btnFace;
    protected RelativeLayout inputLayout;
    protected ImageView btnMultimedia;
    protected Button btnSend;
    protected FuncLayout funcLayout;

    protected EmoticonsFuncView emoticonsFuncView;
    protected EmoticonsIndicator emoticonsIndicator;
    protected EmoticonsToolBar emoticonsToolBar;

    protected boolean dispatchKeyEventPreImeLock = false;

    public XEmoticonsKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflateKeyboardBar();
        initView();
        initFuncView();
    }

    protected void inflateKeyboardBar() {
        inflater.inflate(R.layout.view_keyboard_xhs, this);
    }

    protected View inflateFunc() {
        return inflater.inflate(R.layout.view_func_emoticon, null);
    }

    protected void initView() {
        btnVoiceOrText = ((ImageView) findViewById(R.id.btn_voice_or_text));
        btnVoice = ((Button) findViewById(R.id.btn_voice));
        emoticonsEditText = ((EmoticonsEditText) findViewById(R.id.et_chat));
        btnFace = ((ImageView) findViewById(R.id.btn_face));
        inputLayout = ((RelativeLayout) findViewById(R.id.rl_input));
        btnMultimedia = ((ImageView) findViewById(R.id.btn_multimedia));
        btnSend = ((Button) findViewById(R.id.btn_send));
        funcLayout = ((FuncLayout) findViewById(R.id.ly_kvml));

        btnVoiceOrText.setOnClickListener(this);
        btnFace.setOnClickListener(this);
        btnMultimedia.setOnClickListener(this);
        emoticonsEditText.setOnBackKeyClickListener(this);
    }

    protected void initFuncView() {
        initEmoticonFuncView();
        initEditView();
    }

    protected void initEmoticonFuncView() {
        View keyboardView = inflateFunc();
        funcLayout.addFuncView(FUNC_TYPE_EMOTION, keyboardView);
        emoticonsFuncView = ((EmoticonsFuncView) findViewById(R.id.view_epv));
        emoticonsIndicator = ((EmoticonsIndicatorView) findViewById(R.id.view_eiv));
        emoticonsToolBar = ((EmoticonsToolBar) findViewById(R.id.view_etv));
        emoticonsFuncView.setListener(this);
        emoticonsToolBar.setToolBarItemClickListener(this);
        funcLayout.setOnFuncChangeListener(this);
    }

    protected void initEditView() {
        emoticonsEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!emoticonsEditText.isFocused()) {
                    emoticonsEditText.setFocusable(true);
                    emoticonsEditText.setFocusableInTouchMode(true);
                }
                return false;
            }
        });

        emoticonsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    btnSend.setVisibility(VISIBLE);
                    btnMultimedia.setVisibility(GONE);
                    btnSend.setBackgroundResource(R.drawable.btn_send_bg);
                } else {
                    btnMultimedia.setVisibility(VISIBLE);
                    btnSend.setVisibility(GONE);
                }
            }
        });
    }

    public void setAdapter(EmoticonPacksAdapter adapter) {
        if (adapter != null) {
            List<EmoticonPack<? extends Emoticon>> packList = adapter.getPackList();

            if (packList != null) {
                emoticonsToolBar.setPackList(packList);
            }
        }

        emoticonsFuncView.setAdapter(adapter);
        adapter.setAdapterListener(new EmoticonPacksAdapter.EmoticonPacksAdapterListener() {
            @Override
            public void onDataSetChanged() {
                emoticonsToolBar.notifyDataChanged();
                emoticonsIndicator.notifyDataChanged();
            }
        });
    }

    public void addFuncView(View view) {
        funcLayout.addFuncView(FUNC_TYPE_APPPS, view);
    }

    public void reset() {
        EmoticonsKeyboardUtils.closeSoftKeyboard(this);
        funcLayout.hideAllFuncView();
        btnFace.setImageResource(R.drawable.icon_face_nomal);
    }

    protected void showVoice() {
        inputLayout.setVisibility(GONE);
        btnVoice.setVisibility(VISIBLE);
        reset();
    }

    protected void checkVoice() {
        if (btnVoice.isShown()) {
            btnVoiceOrText.setImageResource(R.drawable.btn_voice_or_text_keyboard);
        } else {
            btnVoiceOrText.setImageResource(R.drawable.btn_voice_or_text);
        }
    }

    protected void showText() {
        inputLayout.setVisibility(VISIBLE);
        btnVoice.setVisibility(GONE);
    }

    protected void toggleFuncView(int key) {
        showText();
        funcLayout.toggleFuncView(key, isSoftKeyboardPop(), emoticonsEditText);
    }

    @Override
    public void onFuncChange(int key) {
        if (FUNC_TYPE_EMOTION == key) {
            btnFace.setImageResource(R.drawable.icon_face_pop);
        } else {
            btnFace.setImageResource(R.drawable.icon_face_nomal);
        }
        checkVoice();
    }

    protected void setFuncViewHeight(int height) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) funcLayout.getLayoutParams();
        params.height = height;
        funcLayout.setLayoutParams(params);
    }

    @Override
    public void onSoftKeyboardHeightChanged(int height) {
        funcLayout.updateHeight(height);
    }

    @Override
    public void onSoftKeyboardPop(int height) {
        super.onSoftKeyboardPop(height);
        funcLayout.setVisibility(true);
        onFuncChange(funcLayout.DEF_KEY);
    }

    @Override
    public void onSoftKeyboardClose() {
        super.onSoftKeyboardClose();
        if (funcLayout.isOnlyShowSoftKeyboard()) {
            reset();
        } else {
            onFuncChange(funcLayout.getCurrentFuncKey());
        }
    }

    public void addOnFuncKeyBoardListener(FuncLayout.FuncKeyBoardListener l) {
        funcLayout.addOnKeyBoardListener(l);
    }

    @Override
    public void onCurrentEmoticonPackChanged(EmoticonPack<? extends Emoticon> currentPack) {
        emoticonsToolBar.selectEmotionPack(currentPack);
    }

    @Override
    public void playTo(int position, EmoticonPack<? extends Emoticon> pack) {
        emoticonsIndicator.playTo(position, pack);
    }

    @Override
    public void playBy(int oldPosition, int newPosition, EmoticonPack<? extends Emoticon> pack) {
        emoticonsIndicator.playBy(oldPosition, newPosition, pack);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_voice_or_text) {
            if (inputLayout.isShown()) {
                btnVoiceOrText.setImageResource(R.drawable.btn_voice_or_text_keyboard);
                showVoice();
            } else {
                showText();
                btnVoiceOrText.setImageResource(R.drawable.btn_voice_or_text);
                EmoticonsKeyboardUtils.openSoftKeyboard(emoticonsEditText);
            }
        } else if (i == R.id.btn_face) {
            toggleFuncView(FUNC_TYPE_EMOTION);
        } else if (i == R.id.btn_multimedia) {
            toggleFuncView(FUNC_TYPE_APPPS);
        }
    }

    @Override
    public void onToolBarItemClick(EmoticonPack<? extends Emoticon> pack) {
        emoticonsFuncView.setCurrentPageSet(pack);
    }

    @Override
    public void onBackKeyClick() {
        if (funcLayout.isShown()) {
            dispatchKeyEventPreImeLock = true;
            reset();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (dispatchKeyEventPreImeLock) {
                    dispatchKeyEventPreImeLock = false;
                    return true;
                }
                if (funcLayout.isShown()) {
                    reset();
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext())) {
            return false;
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext())) {
            return;
        }
        super.requestChildFocus(child, focused);
    }

    public boolean dispatchKeyEventInFullScreen(KeyEvent event) {
        if(event == null){
            return false;
        }
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (EmoticonsKeyboardUtils.isFullScreen((Activity) getContext()) && funcLayout.isShown()) {
                    reset();
                    return true;
                }
            default:
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    boolean isFocused;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        isFocused = emoticonsEditText.getShowSoftInputOnFocus();
                    } else {
                        isFocused = emoticonsEditText.isFocused();
                    }
                    if(isFocused){
                        emoticonsEditText.onKeyDown(event.getKeyCode(), event);
                    }
                }
                return false;
        }
    }

    public EmoticonsEditText getEtChat() { return emoticonsEditText; }

    public Button getBtnVoice() { return btnVoice; }

    public Button getBtnSend() {
        return btnSend;
    }

    public EmoticonsFuncView getEmoticonsFuncView() {
        return emoticonsFuncView;
    }

    public EmoticonsIndicator getEmoticonsIndicatorView() {
        return emoticonsIndicator;
    }

    public EmoticonsToolBar getEmoticonsToolBarView() {
        return emoticonsToolBar;
    }
}
