package github.ll.emoticon.qq;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import github.ll.emoticon.R;
import github.ll.emoticon.common.AdapterUtils;
import github.ll.emoticon.common.data.ImMsgBean;
import github.ll.emoticon.common.filter.QqFilter;
import github.ll.emotionboard.widget.EmoticonsEditText;
import github.ll.emotionboard.widget.FuncLayout;

public class QqActivity extends AppCompatActivity implements FuncLayout.FuncKeyBoardListener {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.lv_chat) ListView lvChat;
    @Bind(R.id.ek_bar) QqEmoticonsKeyBoard ekBar;

    private QqChattingListAdapter chattingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_qq);
        ButterKnife.bind(this);
        toolbar.setTitle("Qq Keyboard");
        initView();
    }

    private void initView() {
        initEmoticonsKeyBoardBar();
        initListView();
    }

    private void initEmoticonsKeyBoardBar() {
        ekBar.getEtChat().addEmoticonFilter(new QqFilter());
        ekBar.setAdapter(QQAdapterUtils.INSTANCE.getCommonAdapter(this,
                AdapterUtils.INSTANCE.getCommonEmoticonClickListener(ekBar.getEtChat())));
        ekBar.addOnFuncKeyBoardListener(this);

        ekBar.addFuncView(QqEmoticonsKeyBoard.FUNC_TYPE_PTT, new SimpleQqGridView(this));
        ekBar.addFuncView(QqEmoticonsKeyBoard.FUNC_TYPE_PTV, new SimpleQqGridView(this));
        ekBar.addFuncView(QqEmoticonsKeyBoard.FUNC_TYPE_PLUG, new SimpleQqGridView(this));

        ekBar.getEtChat().setOnSizeChangedListener(new EmoticonsEditText.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh) {
                scrollToBottom();
            }
        });
        ekBar.getBtnSend().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSendBtnClick(ekBar.getEtChat().getText().toString());
                ekBar.getEtChat().setText("");
            }
        });

        int width = (int) getResources().getDimension(github.ll.emotionboard.R.dimen.bar_tool_btn_width);
        View rightView = LayoutInflater.from(this).inflate(github.ll.emotionboard.R.layout.left_toolbtn, null);
        ImageView iv_icon = (ImageView) rightView.findViewById(github.ll.emotionboard.R.id.iv_icon);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(width, RelativeLayout.LayoutParams.MATCH_PARENT);
        iv_icon.setLayoutParams(imgParams);
        iv_icon.setBackgroundResource(R.mipmap.qvip_emoji_tab_more_new_pressed);
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QqActivity.this, "ADD", Toast.LENGTH_SHORT).show();
            }
        });

        ekBar.getEmoticonsToolBarView().addFixedToolItemView(rightView, true);
    }

    private void initListView() {
        chattingListAdapter = new QqChattingListAdapter(this);
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

    private void OnSendImage(String image) {
        if (!TextUtils.isEmpty(image)) {
            OnSendBtnClick("[img]" + image);
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
