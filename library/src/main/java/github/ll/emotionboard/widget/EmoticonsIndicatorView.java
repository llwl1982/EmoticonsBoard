package github.ll.emotionboard.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import github.ll.emotionboard.R;
import github.ll.emotionboard.data.EmoticonPack;
import github.ll.emotionboard.utils.EmoticonsKeyboardUtils;

public class EmoticonsIndicatorView extends LinearLayout implements github.ll.emotionboard.interfaces.EmoticonsIndicator {

    private static final int MARGIN_LEFT = 4;
    protected Context mContext;
    protected ArrayList<ImageView> mImageViews;
    protected Drawable mDrawableSelect;
    protected Drawable mDrawableNormal;
    protected LayoutParams mLeftLayoutParams;
    public boolean isShowIndicator = true;

    private EmoticonPack<?> mCurrentPack;
    private int mCurrentPosition;

    public EmoticonsIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.setOrientation(HORIZONTAL);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.EmoticonsIndicatorView, 0, 0);

        try {
            mDrawableSelect = a.getDrawable(R.styleable.EmoticonsIndicatorView_bmpSelect);
            mDrawableNormal = a.getDrawable(R.styleable.EmoticonsIndicatorView_bmpNomal);
        } finally {
            a.recycle();
        }

        if(mDrawableNormal == null) {
            mDrawableNormal = getResources().getDrawable(R.drawable.indicator_point_normal);
        }
        if(mDrawableSelect == null) {
            mDrawableSelect = getResources().getDrawable(R.drawable.indicator_point_select);
        }

        mLeftLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLeftLayoutParams.leftMargin = EmoticonsKeyboardUtils.dip2px(context, MARGIN_LEFT);
    }

    @Override
    public void playTo(int position, EmoticonPack<?> pack) {
        if (!checkPack(pack)) {
            return;
        }

        int pagePosition = Math.min(pack.getPageCount(), position);
        mCurrentPosition = pagePosition;

        updateIndicatorCount(pack.getPageCount());

        for (ImageView iv : mImageViews) {
            iv.setImageDrawable(mDrawableNormal);
        }

        mImageViews.get(mCurrentPosition).setImageDrawable(mDrawableSelect);
    }

    @Override
    public void playBy(int startPosition, int nextPosition, EmoticonPack<?> pack) {
        if (!checkPack(pack)) {
            return;
        }

        updateIndicatorCount(pack.getPageCount());

        if (startPosition < 0 || nextPosition < 0 || nextPosition == startPosition) {
            startPosition = nextPosition = 0;
        }

        if (startPosition < 0) {
            startPosition = nextPosition = 0;
        }

        mCurrentPosition = nextPosition;

        final ImageView imageViewStart = mImageViews.get(startPosition);
        final ImageView imageViewNext = mImageViews.get(nextPosition);

        imageViewStart.setImageDrawable(mDrawableNormal);
        imageViewNext.setImageDrawable(mDrawableSelect);
    }

    protected boolean checkPack(EmoticonPack<?> pack) {
        if (pack != null && isShowIndicator) {
            setVisibility(VISIBLE);

            mCurrentPack = pack;
            return true;
        } else {
            setVisibility(GONE);
            return false;
        }
    }

    protected void updateIndicatorCount(int count) {
        if (mImageViews == null) {
            mImageViews = new ArrayList<>();
        }
        if (count > mImageViews.size()) {
            for (int i = mImageViews.size(); i < count; i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setImageDrawable(i == 0 ? mDrawableSelect : mDrawableNormal);
                this.addView(imageView, mLeftLayoutParams);
                mImageViews.add(imageView);
            }
        }
        for (int i = 0; i < mImageViews.size(); i++) {
            if (i >= count) {
                mImageViews.get(i).setVisibility(GONE);
            } else {
                mImageViews.get(i).setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void notifyDataChanged() {
        if (mCurrentPack != null) {
            playTo(mCurrentPosition, mCurrentPack);
        }
    }
}
