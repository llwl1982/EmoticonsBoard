package github.ll.emoticon.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
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
import github.ll.emoticon.common.data.ImMsgBean;
import github.ll.emoticon.common.widget.AutoHeightBehavior;
import github.ll.emoticon.common.widget.SimpleAppsGridView;
import github.ll.emotionboard.XEmoticonsKeyBoard;
import github.ll.emotionboard.widget.AutoHeightLayout;
import github.ll.emotionboard.widget.FuncLayout;

public class SimpleChatCoordinatorLayoutActivity extends AppCompatActivity implements FuncLayout.FuncKeyBoardListener, AutoHeightLayout.OnMaxParentHeightChangeListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.lv_chat)
    ListView lvChat;
    @Bind(R.id.ek_bar)
    XEmoticonsKeyBoard ekBar;

    private ChattingListAdapter chattingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_chat_oncoordinatorlayout);
        ButterKnife.bind(this);
        toolbar.setTitle("Simple Chat Keyboard On CoordinatorLayout");
        setSupportActionBar(toolbar);
        initView();
    }

    private void initView() {
        initEmoticonsKeyBoardBar();
        initListView();
        initCoordinatorLayout();
    }

    private void initEmoticonsKeyBoardBar() {
        SimpleCommonUtils.initEmoticonsEditText(ekBar.getEtChat());
        ekBar.setAdapter(AdapterUtils.INSTANCE.getCommonAdapter(this, null));
        ekBar.addOnFuncKeyBoardListener(this);
        ekBar.addFuncView(new SimpleAppsGridView(this));
        ekBar.setOnMaxParentHeightChangeListener(this);

        ekBar.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSendBtnClick(ekBar.getEtChat().getText().toString());
                ekBar.getEtChat().setText("");
            }
        });
    }

    private void initCoordinatorLayout() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ekBar.getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        if (behavior instanceof AutoHeightBehavior) {
            ((AutoHeightBehavior) behavior).setOnDependentViewChangedListener(new AutoHeightBehavior.OnDependentViewChangedListener() {
                @Override
                public void onDependentViewChangedListener(CoordinatorLayout parent, View child, View dependency) {
                    parent.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollToBottom();
                        }
                    });
                }
            });
        }
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

    private void OnSendBtnClick(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            ImMsgBean bean = new ImMsgBean();
            bean.setContent(msg);
            chattingListAdapter.addData(bean, true, false);
            scrollToBottom();
        }
    }

    private void scrollToBottom() {
        lvChat.post(new Runnable() {
            @Override
            public void run() {
                lvChat.setSelection(lvChat.getBottom());
            }
        });
    }

    @Override
    public void onMaxParentHeightChange(int height) {
        scrollToBottom();
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
