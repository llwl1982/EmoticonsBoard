package github.ll.emoticon.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import github.ll.emotionboard.data.EmoticonPack;
import github.ll.emotionboard.widget.EmoticonsIndicatorView;

public class AnimEmoticonsIndicatorView extends EmoticonsIndicatorView {

    protected AnimatorSet mPlayToAnimatorSet;

    public AnimEmoticonsIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void playTo(int position, EmoticonPack pack) {
        if (!checkPack(pack)) {
            return;
        }

        updateIndicatorCount(pack.getPageCount());

        for (ImageView iv : mImageViews) {
            iv.setImageDrawable(mDrawableNormal);
        }
        mImageViews.get(position).setImageDrawable(mDrawableSelect);
        final ImageView imageViewStrat = mImageViews.get(position);
        ObjectAnimator animIn1 = ObjectAnimator.ofFloat(imageViewStrat, "scaleX", 0.25f, 1.0f);
        ObjectAnimator animIn2 = ObjectAnimator.ofFloat(imageViewStrat, "scaleY", 0.25f, 1.0f);

        if (mPlayToAnimatorSet != null && mPlayToAnimatorSet.isRunning()) {
            mPlayToAnimatorSet.cancel();
            mPlayToAnimatorSet = null;
        }
        mPlayToAnimatorSet = new AnimatorSet();
        mPlayToAnimatorSet.play(animIn1).with(animIn2);
        mPlayToAnimatorSet.setDuration(100);
        mPlayToAnimatorSet.start();
    }
}
