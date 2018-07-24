package github.ll.emotionboard.interfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.EditText;

import java.io.IOException;

public abstract class EmoticonFilter {

    public abstract void filter(EditText editText, CharSequence text, int start, int lengthBefore, int lengthAfter);

    public static Drawable getDrawableFromAssets(Context context, String emoticonName) {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(emoticonName));
            return new BitmapDrawable(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Drawable getDrawable(Context context, int emoticon) {
        if(emoticon <= 0){
            return null;
        }

        return Build.VERSION.SDK_INT >= 21 ? context.getResources().getDrawable(emoticon,null)
                : context.getResources().getDrawable(emoticon);
    }
}
