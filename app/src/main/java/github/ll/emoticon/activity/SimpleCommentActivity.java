package github.ll.emoticon.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import github.ll.emoticon.R;
import github.ll.emoticon.common.AdapterUtils;
import github.ll.emoticon.common.SimpleCommonUtils;
import github.ll.emotionboard.EmoticonsKeyBoardPopWindow;
import github.ll.emotionboard.adpater.EmoticonPacksAdapter;
import github.ll.emotionboard.data.Emoticon;
import github.ll.emotionboard.data.EmoticonPack;
import github.ll.emotionboard.widget.EmoticonsEditText;

public class SimpleCommentActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.et_content) EmoticonsEditText etContent;

    private EmoticonsKeyBoardPopWindow mKeyBoardPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        ButterKnife.bind(this);
        toolbar.setTitle("Simple Comment Keyboard");
        setSupportActionBar(toolbar);
        initEmoticonsEditText();
    }

    private void initEmoticonsEditText() {
        SimpleCommonUtils.initEmoticonsEditText(etContent);
        etContent.setFocusable(true);
        etContent.setFocusableInTouchMode(true);
        etContent.requestFocus();
    }

    private void initKeyBoardPopWindow() {
        mKeyBoardPopWindow = new EmoticonsKeyBoardPopWindow(SimpleCommentActivity.this);

        List<EmoticonPack<Emoticon>> packs = new ArrayList<>();
        packs.add(AdapterUtils.INSTANCE.getEmoji(this));
        packs.add(AdapterUtils.INSTANCE.getXhsPageSetEntity(this));

        EmoticonPacksAdapter adapter = new EmoticonPacksAdapter(packs);
        adapter.setClickListener(AdapterUtils.INSTANCE.getCommonEmoticonClickListener(etContent));
        mKeyBoardPopWindow.setAdapter(adapter);
    }

    @OnClick(R.id.tv_at) void tv_at() {
        Toast.makeText(SimpleCommentActivity.this, "@", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tv_topic) void tv_topic() {
        Toast.makeText(SimpleCommentActivity.this, "#", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iv_face) void iv_face() {
        if (mKeyBoardPopWindow != null && mKeyBoardPopWindow.isShowing()) {
            mKeyBoardPopWindow.dismiss();
        } else {
            if (mKeyBoardPopWindow == null) {
                initKeyBoardPopWindow();
            }
            mKeyBoardPopWindow.showPopupWindow();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && mKeyBoardPopWindow != null && mKeyBoardPopWindow.isShowing()) {
            mKeyBoardPopWindow.dismiss();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mKeyBoardPopWindow != null && mKeyBoardPopWindow.isShowing()) {
            mKeyBoardPopWindow.dismiss();
        }
    }
}
