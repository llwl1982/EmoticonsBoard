package github.ll.emotionboard.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

import github.ll.emotionboard.adpater.EmoticonPacksAdapter;
import github.ll.emotionboard.data.Emoticon;
import github.ll.emotionboard.data.EmoticonPack;


public class EmoticonsFuncView extends ViewPager {

    private static final String TAG = "EmoticonsFuncView";

    protected EmoticonPacksAdapter mAdapter;
    protected int mCurrentPagePosition;

    public EmoticonsFuncView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setAdapter(EmoticonPacksAdapter adapter) {
        super.setAdapter(adapter);
        
        mAdapter = adapter;

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled position:" + position + " positionOffset:" + positionOffset);
                if (Math.abs(positionOffset) < 0.000001) {
                    checkPageChange(position);
                    mCurrentPagePosition = position;
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected position:" + position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (mEmoticonsFuncListener == null
                || mAdapter.getCount() == 0) {
            return;
        }
        
        EmoticonPack<?> pack = mAdapter.getPackList().get(0);
        mEmoticonsFuncListener.playTo(0, pack);
        mEmoticonsFuncListener.onCurrentEmoticonPackChanged(pack);
    }

    public void setCurrentPageSet(EmoticonPack<? extends Emoticon> pack) {
        if (mAdapter == null || mAdapter.getCount() <= 0) {
            return;
        }
        
        setCurrentItem(mAdapter.getEmoticonPackPosition(pack));
    }

    private void checkPageChange(int position) {
        Log.d(TAG, "checkPageChange");
        if (mAdapter == null) {
            return;
        }
        int start = 0;
        for (EmoticonPack<?> pack : mAdapter.getPackList()) {

            int size = pack.getPageCount();

            int end = start + size - 1;

            if (position <= end) {

                if (mEmoticonsFuncListener != null) {
                    mEmoticonsFuncListener.playTo(position - start, pack);
                }

//                if (mCurrentPagePosition - start >= size) {
//                    if (mEmoticonsFuncListener != null) {
//                        mEmoticonsFuncListener.playTo(position - start, pack);
//                    }
//                }
//
//                else if (mCurrentPagePosition - start < 0) {
//                    if (mEmoticonsFuncListener != null) {
//                        mEmoticonsFuncListener.playTo(0, pack);
//                    }
//                }
//
//                // 当前表情集
//                else {
//                    if (mEmoticonsFuncListener != null) {
//                        mEmoticonsFuncListener.playBy(mCurrentPagePosition - start, position - start, pack);
//                    }
//                }

                if (mEmoticonsFuncListener != null) {
                    mEmoticonsFuncListener.onCurrentEmoticonPackChanged(pack);
                }

                return;
            }
            start += size;
        }
    }

    private EmoticonsFuncListener mEmoticonsFuncListener;

    public void setListener(EmoticonsFuncListener listener) {
        mEmoticonsFuncListener = listener;
    }


    public interface EmoticonsFuncListener {

        /**
         * 切换当前显示的表情包
         * @param currentPack
         */
        void onCurrentEmoticonPackChanged(EmoticonPack<? extends Emoticon> currentPack);

        /**
         * 移动到表情集
         * @param position 目的表情集的位置
         * @param pack
         */
        void playTo(int position, EmoticonPack<? extends Emoticon> pack);

        /**
         * 在当前表情集中移动
         * @param oldPosition 移动的开始位置
         * @param newPosition 移动的结束为止
         * @param pack
         */
        void playBy(int oldPosition, int newPosition, EmoticonPack<? extends Emoticon> pack);
    }
}
