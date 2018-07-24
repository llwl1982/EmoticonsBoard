package github.ll.emotionboard.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import github.ll.emotionboard.adpater.EmoticonPacksAdapter;
import github.ll.emotionboard.data.Emoticon;
import github.ll.emotionboard.data.EmoticonPack;


public class EmoticonsFuncView extends ViewPager {

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
            }

            @Override
            public void onPageSelected(int position) {
                checkPageChange(position);
                mCurrentPagePosition = position;
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
        if (mAdapter == null) {
            return;
        }
        int end = 0;
        for (EmoticonPack<?> pack : mAdapter.getPackList()) {

            int size = pack.getPageCount();

            if (end + size > position) {

                boolean isEmoticonSetChanged = true;
                // 上一表情集
                if (mCurrentPagePosition - end >= size) {
                    if (mEmoticonsFuncListener != null) {
                        mEmoticonsFuncListener.playTo(position - end, pack);
                    }
                }
                // 下一表情集
                else if (mCurrentPagePosition - end < 0) {
                    if (mEmoticonsFuncListener != null) {
                        mEmoticonsFuncListener.playTo(0, pack);
                    }
                }
                // 当前表情集
                else {
                    if (mEmoticonsFuncListener != null) {
                        mEmoticonsFuncListener.playBy(mCurrentPagePosition - end, position - end, pack);
                    }
                    isEmoticonSetChanged = false;
                }

                if (isEmoticonSetChanged && mEmoticonsFuncListener != null) {
                    mEmoticonsFuncListener.onCurrentEmoticonPackChanged(pack);
                }
                return;
            }
            end += size;
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
