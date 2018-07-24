package github.ll.emoticon.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import github.ll.emoticon.R;
import github.ll.emoticon.common.adapter.FuncsAdapter;
import github.ll.emoticon.common.data.AppBean;

public class SimpleAppsGridView extends RelativeLayout {

    protected View view;

    public SimpleAppsGridView(Context context) {
        this(context, null);
    }

    public SimpleAppsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_apps, this);
        init();
    }

    protected void init(){
        GridView gv_apps = (GridView) view.findViewById(R.id.gv_apps);
        ArrayList<AppBean> mAppBeanList = new ArrayList<>();
        mAppBeanList.add(new AppBean(R.mipmap.icon_photo, "图片"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_camera, "拍照"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_audio, "视频"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_qzone, "空间"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_contact, "联系人"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_file, "文件"));
        mAppBeanList.add(new AppBean(R.mipmap.icon_loaction, "位置"));
        FuncsAdapter adapter = new FuncsAdapter(getContext(), mAppBeanList);
        gv_apps.setAdapter(adapter);
    }
}
