package github.ll.emotionboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import github.ll.emotionboard.adpater.EmoticonPacksAdapter;
import github.ll.emotionboard.data.EmoticonPack;
import github.ll.emotionboard.interfaces.EmoticonsIndicator;
import github.ll.emotionboard.interfaces.EmoticonsToolBar;
import github.ll.emotionboard.interfaces.OnToolBarItemClickListener;
import github.ll.emotionboard.utils.EmoticonsKeyboardUtils;
import github.ll.emotionboard.widget.EmoticonsFuncView;

public class EmoticonsKeyBoardPopWindow extends PopupWindow implements EmoticonsFuncView.EmoticonsFuncListener, OnToolBarItemClickListener {

    private Context mContext;
    protected EmoticonsFuncView emoticonsFuncView;
    protected EmoticonsIndicator emoticonsIndicator;
    protected EmoticonsToolBar emoticonsToolBar;

    public EmoticonsKeyBoardPopWindow(Context context) {
        super(context, null);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.view_func_emoticon, null);

        setContentView(contentView);
        setWidth(EmoticonsKeyboardUtils.getDisplayWidthPixels(mContext));
        setHeight(EmoticonsKeyboardUtils.getDefKeyboardHeight(mContext));
        setAnimationStyle(R.style.PopupAnimation);
        setOutsideTouchable(true);
        update();
        ColorDrawable dw = new ColorDrawable(0000000000);
        setBackgroundDrawable(dw);

        updateView(contentView);
    }

    private void updateView(View view) {
        emoticonsFuncView = ((EmoticonsFuncView) view.findViewById(R.id.view_epv));
        emoticonsIndicator = (EmoticonsIndicator) view.findViewById(R.id.view_eiv);
        emoticonsToolBar = (EmoticonsToolBar) view.findViewById(R.id.view_etv);
        emoticonsFuncView.setListener(this);
        emoticonsToolBar.setToolBarItemClickListener(this);
    }

    public void setAdapter(EmoticonPacksAdapter adapter) {
        if (adapter != null) {
            emoticonsToolBar.setPackList(adapter.getPackList());
            emoticonsFuncView.setAdapter(adapter);
        }

    }

    public void showPopupWindow() {
        View rootView = EmoticonsKeyboardUtils.getRootView((Activity) mContext);
        if (this.isShowing()) {
            this.dismiss();
        } else {
            EmoticonsKeyboardUtils.closeSoftKeyboard(mContext);
            this.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void onCurrentEmoticonPackChanged(EmoticonPack currentPack) {
        emoticonsToolBar.selectEmotionPack(currentPack);
    }

    @Override
    public void playTo(int position, EmoticonPack pack) {
        emoticonsIndicator.playTo(position, pack);
    }

    @Override
    public void playBy(int oldPosition, int newPosition, EmoticonPack pack) {
        emoticonsIndicator.playBy(oldPosition, newPosition, pack);
    }

    @Override
    public void onToolBarItemClick(EmoticonPack pack) {
        emoticonsFuncView.setCurrentPageSet(pack);
    }
}
