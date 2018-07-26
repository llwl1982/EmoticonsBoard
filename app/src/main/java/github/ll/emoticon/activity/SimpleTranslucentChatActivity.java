package github.ll.emoticon.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import github.ll.emoticon.R;
import github.ll.emoticon.common.AdapterUtils;
import github.ll.emoticon.common.SimpleCommonUtils;
import github.ll.emoticon.common.adapter.ChattingListAdapter;
import github.ll.emoticon.common.adapter.emoticonadapter.DeleteEmoticon;
import github.ll.emoticon.common.data.BigEmoticon;
import github.ll.emoticon.common.data.ImMsgBean;
import github.ll.emoticon.common.data.PlaceHoldEmoticon;
import github.ll.emoticon.common.widget.SimpleAppsGridView;
import github.ll.emotionboard.EmoticonsBoard;
import github.ll.emotionboard.data.Emoticon;
import github.ll.emotionboard.interfaces.OnEmoticonClickListener;
import github.ll.emotionboard.utils.EmoticonsKeyboardUtils;
import github.ll.emotionboard.widget.EmoticonsEditText;
import github.ll.emotionboard.widget.FuncLayout;

import static github.ll.emoticon.common.data.ImMsgBean.CHAT_MSGTYPE_IMG;
import static github.ll.emoticon.common.data.ImMsgBean.CHAT_MSGTYPE_TEXT;

public class SimpleTranslucentChatActivity extends AppCompatActivity implements FuncLayout.FuncKeyBoardListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.lv_chat) ListView lvChat;
    @Bind(R.id.ek_bar)
    EmoticonsBoard ekBar;

    private ChattingListAdapter chattingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_simple_chat_translucent);
        ButterKnife.bind(this);
        toolbar.setTitle("Simple Chat Keyboard");
        initView();
    }

    private void initView() {
        initEmoticonsKeyBoardBar();
        initListView();
    }

    private void initEmoticonsKeyBoardBar() {
        SimpleCommonUtils.initEmoticonsEditText(ekBar.getEtChat());
        ekBar.setAdapter(AdapterUtils.INSTANCE.getCommonAdapter(this, onEmoticonClickListener));
        ekBar.addOnFuncKeyBoardListener(this);
        ekBar.addFuncView(new SimpleAppsGridView(this));

        ekBar.getEtChat().setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh) {
                scrollToBottom();
            }
        });
        ekBar.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBtnClick(ekBar.getEtChat().getText().toString());
                ekBar.getEtChat().setText("");
            }
        });
    }

    OnEmoticonClickListener onEmoticonClickListener = new OnEmoticonClickListener<Emoticon>() {
        @Override
        public void onEmoticonClick(Emoticon emoticon) {

            if (emoticon == null) {
                return;
            }

            if (emoticon instanceof DeleteEmoticon) {
                SimpleCommonUtils.delClick(ekBar.getEtChat());
            } else if (emoticon instanceof PlaceHoldEmoticon) {
                // do nothing
            } else if (emoticon instanceof BigEmoticon) {
                sendImage(emoticon.getUri());
            } else {
                String content = emoticon.getCode();
                if (TextUtils.isEmpty(content)) {
                    return;
                }

                int index = ekBar.getEtChat().getSelectionStart();
                Editable editable = ekBar.getEtChat().getText();
                editable.insert(index, content);
            }
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(EmoticonsKeyboardUtils.isFullScreen(this)){
            boolean isConsum = ekBar.dispatchKeyEventInFullScreen(event);
            return isConsum ? isConsum : super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    private void initListView() {
        chattingListAdapter = new ChattingListAdapter(this);
        List<ImMsgBean> beanList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ImMsgBean bean = new ImMsgBean();
            bean.setContent("Test:" + i);
            beanList.add(bean);
        }
        chattingListAdapter.addData(beanList);
        lvChat.setAdapter(chattingListAdapter);
        lvChat.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_IDLE:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        ekBar.reset();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void sendBtnClick(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            ImMsgBean bean = new ImMsgBean();
            bean.setMsgType(CHAT_MSGTYPE_TEXT);
            bean.setContent(msg);
            sendMsg(bean);
        }
    }

    private void sendMsg(ImMsgBean bean) {
        chattingListAdapter.addData(bean, true, false);
        scrollToBottom();
    }

    private void sendImage(String image) {
        if (!TextUtils.isEmpty(image)) {
            ImMsgBean bean = new ImMsgBean();
            bean.setMsgType(CHAT_MSGTYPE_IMG);
            bean.setImage(image);
            sendMsg(bean);
        }
    }

    private void scrollToBottom() {
        lvChat.requestLayout();
        lvChat.post(new Runnable() {
            @Override
            public void run() {
                lvChat.setSelection(lvChat.getBottom());
            }
        });
    }

    @Override
    public void onFuncPop(int height) {
        scrollToBottom();
    }

    @Override
    public void onFuncClose() { }

    @Override
    protected void onPause() {
        super.onPause();
        ekBar.reset();
    }
}
